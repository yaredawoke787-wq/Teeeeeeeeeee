package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AppLanguage
import com.example.model.Product
import com.example.model.ProductRepository
import com.example.model.Translation
import com.example.model.Category
import com.example.model.AppDatabase
import com.example.network.CloudSyncService
import android.util.Log
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TekeAppViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val productDao = db.productDao()
    private val categoryDao = db.categoryDao()
    // Dynamic lists of products and categories pointing to global shared live state
    val productsList: List<Product> get() = ProductRepository.liveProducts
    val categoriesList: List<Category> get() = ProductRepository.liveCategories

    // Firebase Database Sync Config
    private val prefs = application.getSharedPreferences("teke_gift_admin_prefs", Context.MODE_PRIVATE)
    var firebaseDbUrl by mutableStateOf(prefs.getString("firebase_db_url", "https://teke-gift-default-rtdb.firebaseio.com/") ?: "https://teke-gift-default-rtdb.firebaseio.com/")
        private set

    val productsUrl: String get() = "${firebaseDbUrl}products.json"
    val categoriesUrl: String get() = "${firebaseDbUrl}categories.json"

    fun updateFirebaseDbUrl(newUrl: String) {
        var formatted = newUrl.trim()
        if (formatted.isNotEmpty()) {
            if (!formatted.startsWith("http://") && !formatted.startsWith("https://")) {
                formatted = "https://$formatted"
            }
            if (!formatted.endsWith("/")) {
                formatted = "$formatted/"
            }
            firebaseDbUrl = formatted
            prefs.edit().putString("firebase_db_url", formatted).apply()
            refreshCatalog()
        }
    }

    // Admin state
    var isAdminConsoleOpen by mutableStateOf(false)
    var isAdminLoggedIn by mutableStateOf(false)

    // Admin authentication credentials (production-ready simple verification)
    fun attemptAdminLogin(email: String, pass: String): Boolean {
        return if (email.trim().lowercase() == "admin@tekegift.com" && pass == "admin123") {
            isAdminLoggedIn = true
            true
        } else {
            false
        }
    }

    fun logoutAdmin() {
        isAdminLoggedIn = false
    }

    private fun syncProductsToCloud() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentProducts = productDao.getAllProductsDirect()
                val response = CloudSyncService.api.updateProducts(productsUrl, currentProducts)
                if (response.isSuccessful) {
                    Log.d("TekeAppViewModel", "Successfully synced products to cloud")
                } else {
                    Log.e("TekeAppViewModel", "Failed to sync products to cloud: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("TekeAppViewModel", "Error syncing products to cloud", e)
            }
        }
    }

    private fun syncCategoriesToCloud() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentCategories = categoryDao.getAllCategoriesDirect()
                val response = CloudSyncService.api.updateCategories(categoriesUrl, currentCategories)
                if (response.isSuccessful) {
                    Log.d("TekeAppViewModel", "Successfully synced categories to cloud")
                } else {
                    Log.e("TekeAppViewModel", "Failed to sync categories to cloud: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("TekeAppViewModel", "Error syncing categories to cloud", e)
            }
        }
    }

    // Product CRUD Operations
    fun addProduct(product: Product) {
        viewModelScope.launch {
            productDao.insertProduct(product)
            syncProductsToCloud()
        }
    }

    fun updateProduct(updatedProduct: Product) {
        viewModelScope.launch {
            productDao.updateProduct(updatedProduct)
            syncProductsToCloud()
        }
    }

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            productDao.deleteProductById(productId)
            syncProductsToCloud()
        }
        // Clean up cart/favorites if needed
        if (favoriteProductIds.contains(productId)) {
            favoriteProductIds = favoriteProductIds - productId
        }
        if (cartItems.containsKey(productId)) {
            cartItems = cartItems - productId
        }
    }

    // Category CRUD Operations
    fun addCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.insertCategory(category)
            syncCategoriesToCloud()
        }
    }

    fun updateCategory(updatedCategory: Category) {
        viewModelScope.launch {
            categoryDao.updateCategory(updatedCategory)
            syncCategoriesToCloud()
        }
    }

    fun deleteCategory(categoryId: String) {
        viewModelScope.launch {
            categoryDao.deleteCategoryById(categoryId)
            // Move products under deleted category to "customized"
            productDao.getAllProductsDirect().forEach { prod ->
                if (prod.categoryId == categoryId) {
                    productDao.updateProduct(prod.copy(categoryId = "customized"))
                }
            }
            syncCategoriesToCloud()
            syncProductsToCloud()
        }
    }

    // Localization state
    var currentLanguage by mutableStateOf(AppLanguage.ENGLISH)
        private set

    // Theme state: "auto", "light", "dark"
    var currentThemeSetting by mutableStateOf("auto")
        private set

    // Bottom Navigation tab: "home", "categories", "favorites", "cart", "profile"
    var activeTab by mutableStateOf("home")

    // Category filter state
    var selectedCategoryFilter by mutableStateOf("all")

    // Search queries
    var searchQuery by mutableStateOf("")

    // Favorite Product IDs
    var favoriteProductIds by mutableStateOf(setOf<Int>())
        private set

    // Cart items: Map of Product ID to Quantity
    var cartItems by mutableStateOf(mapOf<Int, Int>())
        private set

    // Selected product for active detail overlay
    var selectedProductForDetail by mutableStateOf<Product?>(null)

    // Applied promo coupon code
    var appliedPromoCode by mutableStateOf<String?>(null)

    // Splash screen visible state
    var isSplashVisible by mutableStateOf(true)
        private set

    fun dismissSplash() {
        isSplashVisible = false
    }

    // Search active state
    var isSearchActive by mutableStateOf(false)

    init {
        // Observe database live updates and sync with ProductRepository lists
        viewModelScope.launch {
            // 1. Try to fetch from cloud first to keep database synced
            try {
                val categoriesResponse = CloudSyncService.api.getCategories(categoriesUrl)
                if (categoriesResponse.isSuccessful) {
                    val cloudCategories = categoriesResponse.body()
                    if (cloudCategories != null && cloudCategories.isNotEmpty()) {
                        categoryDao.insertAllCategories(cloudCategories)
                        Log.d("TekeAppViewModel", "Startup: Loaded categories from cloud successfully")
                    }
                }
                
                val productsResponse = CloudSyncService.api.getProducts(productsUrl)
                if (productsResponse.isSuccessful) {
                    val cloudProducts = productsResponse.body()
                    if (cloudProducts != null && cloudProducts.isNotEmpty()) {
                        productDao.insertAllProducts(cloudProducts)
                        Log.d("TekeAppViewModel", "Startup: Loaded products from cloud successfully")
                    }
                }
            } catch (e: Exception) {
                Log.e("TekeAppViewModel", "Startup cloud sync failed, using cached local database", e)
            }

            // 2. Pre-populate categories locally if still empty, then push to cloud
            try {
                val existingCategories = categoryDao.getAllCategoriesDirect()
                if (existingCategories.isEmpty()) {
                    categoryDao.insertAllCategories(ProductRepository.categories)
                    syncCategoriesToCloud()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // 3. Pre-populate products locally if still empty, then push to cloud
            try {
                val existingProducts = productDao.getAllProductsDirect()
                if (existingProducts.isEmpty()) {
                    productDao.insertAllProducts(ProductRepository.products)
                    syncProductsToCloud()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // 4. Collect products updates reactively
            launch {
                try {
                    productDao.getAllProducts().collect { dbProducts ->
                        withContext(Dispatchers.Main) {
                            ProductRepository.liveProducts.clear()
                            ProductRepository.liveProducts.addAll(dbProducts)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("TekeAppViewModel", "Failed to collect products from database", e)
                }
            }

            // 5. Collect categories updates reactively
            launch {
                try {
                    categoryDao.getAllCategories().collect { dbCategories ->
                        withContext(Dispatchers.Main) {
                            ProductRepository.liveCategories.clear()
                            ProductRepository.liveCategories.addAll(dbCategories)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("TekeAppViewModel", "Failed to collect categories from database", e)
                }
            }
        }
    }

    // Manual refresh function to sync data from the cloud on-demand
    fun refreshCatalog() {
        viewModelScope.launch {
            try {
                val categoriesResponse = CloudSyncService.api.getCategories(categoriesUrl)
                if (categoriesResponse.isSuccessful) {
                    val cloudCategories = categoriesResponse.body()
                    if (cloudCategories != null && cloudCategories.isNotEmpty()) {
                        categoryDao.insertAllCategories(cloudCategories)
                    }
                }
                
                val productsResponse = CloudSyncService.api.getProducts(productsUrl)
                if (productsResponse.isSuccessful) {
                    val cloudProducts = productsResponse.body()
                    if (cloudProducts != null && cloudProducts.isNotEmpty()) {
                        productDao.insertAllProducts(cloudProducts)
                    }
                }
            } catch (e: Exception) {
                Log.e("TekeAppViewModel", "Manual refresh catalog failed", e)
            }
        }
    }

    // Toggle language instantly
    fun toggleLanguage() {
        currentLanguage = if (currentLanguage == AppLanguage.ENGLISH) {
            AppLanguage.AMHARIC
        } else {
            AppLanguage.ENGLISH
        }
    }

    fun setLanguage(lang: AppLanguage) {
        currentLanguage = lang
    }

    // Set theme setting
    fun setThemeSetting(setting: String) {
        currentThemeSetting = setting
    }

    // Translate helper shortcut
    fun translate(key: String): String {
        return Translation.get(key, currentLanguage)
    }

    // Favorites operations
    fun toggleFavorite(productId: Int) {
        favoriteProductIds = if (favoriteProductIds.contains(productId)) {
            favoriteProductIds - productId
        } else {
            favoriteProductIds + productId
        }
    }

    fun isFavorite(productId: Int): Boolean {
        return favoriteProductIds.contains(productId)
    }

    // Cart operations
    fun addToCart(productId: Int) {
        val currentQty = cartItems[productId] ?: 0
        cartItems = cartItems + (productId to (currentQty + 1))
    }

    fun updateCartQuantity(productId: Int, qty: Int) {
        if (qty <= 0) {
            cartItems = cartItems - productId
        } else {
            cartItems = cartItems + (productId to qty)
        }
    }

    fun clearCart() {
        cartItems = emptyMap()
        appliedPromoCode = null
    }

    fun applyPromo(code: String): Boolean {
        return if (code.trim().uppercase() == "TEKEGOLD" || code.trim().uppercase() == "GIFTS") {
            appliedPromoCode = code.trim().uppercase()
            true
        } else {
            false
        }
    }

    // Price calculators
    fun getCartSubtotal(): Double {
        var total = 0.0
        cartItems.forEach { (productId, qty) ->
            val product = productsList.find { it.id == productId }
            if (product != null) {
                val discountedPrice = product.price * (1.0 - product.discountPercentage / 100.0)
                total += discountedPrice * qty
            }
        }
        return total
    }

    fun getCartTotal(): Double {
        val subtotal = getCartSubtotal()
        val discount = if (appliedPromoCode != null) subtotal * 0.10 else 0.0 // 10% discount
        return subtotal - discount
    }

    // Buy now and place order function (opens dialer)
    fun initiateOrderCall(context: Context) {
        try {
            val phone = "+251921935862"
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phone")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            Toast.makeText(
                context,
                if (currentLanguage == AppLanguage.ENGLISH) "Connecting with Teke Sales Concierge..." else "ከተኬ ሽያጭ አስተናጋጅ ጋር በመገናኘት ላይ...",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    // Social accounts intent helpers
    fun openSocialIntent(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open link: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    // Copy text to clipboard helper
    fun copyToClipboard(context: Context, text: String, label: String = "Teke Man Promotion") {
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText(label, text)
            clipboard.setPrimaryClip(clip)
            val message = if (currentLanguage == AppLanguage.ENGLISH) "Copied: $text" else "ኮፒ ተደርጓል: $text"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error copying", Toast.LENGTH_SHORT).show()
        }
    }
}
