package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Product
import com.example.ui.components.GlassCard
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.PremiumRose
import com.example.ui.theme.PureWhite
import com.example.viewmodel.TekeAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminConsoleOverlay(
    viewModel: TekeAppViewModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isEditingProduct by remember { mutableStateOf<Product?>(null) }
    var isAddingProduct by remember { mutableStateOf(false) }

    // State variables for Login
    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0C0A09)) // Luxury ultra-dark obsidian
    ) {
        if (!viewModel.isAdminLoggedIn) {
            // Elegant Admin Login Screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                
                // Studio Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(LuxuryGold.copy(alpha = 0.1f), shape = CircleShape)
                        .border(1.5.dp, LuxuryGold, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock icon",
                        tint = LuxuryGold,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "TEKE ADMIN GATEWAY",
                    color = LuxuryGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sign in to access catalog controls",
                    color = PureWhite.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Email field
                OutlinedTextField(
                    value = loginEmail,
                    onValueChange = { loginEmail = it },
                    label = { Text("Admin Email", color = PureWhite.copy(alpha = 0.4f)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LuxuryGold,
                        unfocusedBorderColor = PureWhite.copy(alpha = 0.15f),
                        focusedLabelColor = LuxuryGold,
                        unfocusedLabelColor = PureWhite.copy(alpha = 0.4f),
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password field
                OutlinedTextField(
                    value = loginPassword,
                    onValueChange = { loginPassword = it },
                    label = { Text("Password", color = PureWhite.copy(alpha = 0.4f)) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LuxuryGold,
                        unfocusedBorderColor = PureWhite.copy(alpha = 0.15f),
                        focusedLabelColor = LuxuryGold,
                        unfocusedLabelColor = PureWhite.copy(alpha = 0.4f),
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Login trigger
                Button(
                    onClick = {
                        val success = viewModel.attemptAdminLogin(loginEmail, loginPassword)
                        if (success) {
                            Toast.makeText(context, "Access Granted", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold, contentColor = Color.Black),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("AUTHORIZE ACCESS", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Back button
                TextButton(onClick = onDismiss) {
                    Text("Return to App", color = PureWhite.copy(alpha = 0.5f))
                }
            }
        } else {
            // Logged in: Main Admin Panel List of Products
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top Custom Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onDismiss) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = LuxuryGold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "TEKE PROMOTION ADMIN",
                                color = LuxuryGold,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Manage app inventory and promo streams",
                                color = PureWhite.copy(alpha = 0.5f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Row {
                        // Logout Icon button
                        IconButton(onClick = { viewModel.logoutAdmin() }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", tint = PremiumRose)
                        }
                    }
                }

                // Sub-Actions Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PRODUCTS CATALOG",
                        color = PureWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Button(
                        onClick = { isAddingProduct = true },
                        colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Product", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider(color = PureWhite.copy(alpha = 0.08f), thickness = 1.dp)

                // List of Products
                val allProducts = viewModel.productsList
                if (allProducts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Default.Inbox, contentDescription = "empty", tint = PureWhite.copy(alpha = 0.2f), modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No products found", color = PureWhite.copy(alpha = 0.4f), fontSize = 14.sp)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(allProducts, key = { it.id }) { prod ->
                            ProductAdminRow(
                                product = prod,
                                onEdit = { isEditingProduct = prod },
                                onDelete = {
                                    viewModel.deleteProduct(prod.id)
                                    Toast.makeText(context, "Product Deleted", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }

            // Slide-up Form Sheet for Adding or Editing Product
            if (isAddingProduct || isEditingProduct != null) {
                ProductFormDialog(
                    productToEdit = isEditingProduct,
                    onDismiss = {
                        isAddingProduct = false
                        isEditingProduct = null
                    },
                    onSave = { updatedOrNewProduct ->
                        if (isEditingProduct != null) {
                            viewModel.updateProduct(updatedOrNewProduct)
                            Toast.makeText(context, "Product updated successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.addProduct(updatedOrNewProduct)
                            Toast.makeText(context, "Product added successfully!", Toast.LENGTH_SHORT).show()
                        }
                        isAddingProduct = false
                        isEditingProduct = null
                    }
                )
            }
        }
    }
}

@Composable
fun ProductAdminRow(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundAlpha = 0.08f
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail placeholder or visual
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1E1B18)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.CardGiftcard, contentDescription = null, tint = LuxuryGold.copy(alpha = 0.4f))
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details and actions
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = product.titleEn,
                        color = PureWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Price: ${product.price.toInt()} ETB | Disc: ${product.discountPercentage}%",
                        color = PureWhite.copy(alpha = 0.5f),
                        fontSize = 11.sp
                    )
                }

                // Video status label
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(
                                if (product.videoUrl.isNotBlank()) Color(0xFF10B981) else PremiumRose,
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (product.videoUrl.isNotBlank()) "Video Attached" else "No video attached",
                        color = if (product.videoUrl.isNotBlank()) Color(0xFF10B981) else PremiumRose.copy(alpha = 0.8f),
                        fontSize = 10.sp
                    )
                }
            }

            // Action Buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = LuxuryGold, modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = PremiumRose, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun ProductFormDialog(
    productToEdit: Product?,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit
) {
    var titleEn by remember { mutableStateOf(productToEdit?.titleEn ?: "") }
    var titleAm by remember { mutableStateOf(productToEdit?.titleAm ?: "") }
    var descEn by remember { mutableStateOf(productToEdit?.descriptionEn ?: "") }
    var descAm by remember { mutableStateOf(productToEdit?.descriptionAm ?: "") }
    var priceText by remember { mutableStateOf(productToEdit?.price?.toInt()?.toString() ?: "") }
    var discountText by remember { mutableStateOf(productToEdit?.discountPercentage?.toString() ?: "0") }
    var categoryId by remember { mutableStateOf(productToEdit?.categoryId ?: "flowers") }
    var imageUrl by remember { mutableStateOf(productToEdit?.imageUrl ?: "") }
    var videoUrl by remember { mutableStateOf(productToEdit?.videoUrl ?: "") }

    val scrollState = rememberScrollState()

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(top = 40.dp) // Leave status bar space
        ) {
            GlassCard(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                backgroundAlpha = 0.95f
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    // Title Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (productToEdit != null) Icons.Default.EditNote else Icons.Default.PostAdd,
                                contentDescription = null,
                                tint = LuxuryGold,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (productToEdit != null) "EDIT PRODUCT" else "ADD PRODUCT",
                                color = LuxuryGold,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = PureWhite)
                        }
                    }

                    HorizontalDivider(color = PureWhite.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 12.dp))

                    // Input Form Fields (Scrollable Column)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Title English
                        OutlinedTextField(
                            value = titleEn,
                            onValueChange = { titleEn = it },
                            label = { Text("Product Title (English)", color = PureWhite.copy(alpha = 0.5f)) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LuxuryGold,
                                unfocusedBorderColor = PureWhite.copy(alpha = 0.15f),
                                focusedTextColor = PureWhite,
                                unfocusedTextColor = PureWhite
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Title Amharic
                        OutlinedTextField(
                            value = titleAm,
                            onValueChange = { titleAm = it },
                            label = { Text("Product Title (Amharic)", color = PureWhite.copy(alpha = 0.5f)) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LuxuryGold,
                                unfocusedBorderColor = PureWhite.copy(alpha = 0.15f),
                                focusedTextColor = PureWhite,
                                unfocusedTextColor = PureWhite
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Description English
                        OutlinedTextField(
                            value = descEn,
                            onValueChange = { descEn = it },
                            label = { Text("Description (English)", color = PureWhite.copy(alpha = 0.5f)) },
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LuxuryGold,
                                unfocusedBorderColor = PureWhite.copy(alpha = 0.15f),
                                focusedTextColor = PureWhite,
                                unfocusedTextColor = PureWhite
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Description Amharic
                        OutlinedTextField(
                            value = descAm,
                            onValueChange = { descAm = it },
                            label = { Text("Description (Amharic)", color = PureWhite.copy(alpha = 0.5f)) },
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LuxuryGold,
                                unfocusedBorderColor = PureWhite.copy(alpha = 0.15f),
                                focusedTextColor = PureWhite,
                                unfocusedTextColor = PureWhite
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Price and Discount row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            OutlinedTextField(
                                value = priceText,
                                onValueChange = { priceText = it },
                                label = { Text("Price (ETB)", color = PureWhite.copy(alpha = 0.5f)) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LuxuryGold,
                                    unfocusedBorderColor = PureWhite.copy(alpha = 0.15f),
                                    focusedTextColor = PureWhite,
                                    unfocusedTextColor = PureWhite
                                ),
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = discountText,
                                onValueChange = { discountText = it },
                                label = { Text("Discount %", color = PureWhite.copy(alpha = 0.5f)) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LuxuryGold,
                                    unfocusedBorderColor = PureWhite.copy(alpha = 0.15f),
                                    focusedTextColor = PureWhite,
                                    unfocusedTextColor = PureWhite
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Category ID field selection
                        Text("Select Category", color = LuxuryGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("flowers" to "Flowers", "anniversary" to "Anniversary", "birthday" to "Birthday", "corporates" to "Corporates", "customized" to "Customized").forEach { (id, label) ->
                                val isSel = categoryId == id
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) LuxuryGold else Color.White.copy(alpha = 0.05f))
                                        .border(1.dp, if (isSel) LuxuryGold else PureWhite.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                        .clickable { categoryId = id }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = label,
                                        color = if (isSel) Color.Black else PureWhite,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Video URL field (CRITICAL NEW FIELD REQUESTED BY THE USER!)
                        OutlinedTextField(
                            value = videoUrl,
                            onValueChange = { videoUrl = it },
                            label = { Text("Promo Video Link (.mp4)", color = PureWhite.copy(alpha = 0.5f)) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LuxuryGold,
                                unfocusedBorderColor = PureWhite.copy(alpha = 0.15f),
                                focusedTextColor = PureWhite,
                                unfocusedTextColor = PureWhite
                            ),
                            placeholder = { Text("https://example.com/video.mp4", color = PureWhite.copy(alpha = 0.25f)) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Image URL field
                        OutlinedTextField(
                            value = imageUrl,
                            onValueChange = { imageUrl = it },
                            label = { Text("Image Link / Thumbnail", color = PureWhite.copy(alpha = 0.5f)) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LuxuryGold,
                                unfocusedBorderColor = PureWhite.copy(alpha = 0.15f),
                                focusedTextColor = PureWhite,
                                unfocusedTextColor = PureWhite
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Save / Submit Bar
                    Button(
                        onClick = {
                            val parsedPrice = priceText.toDoubleOrNull() ?: 0.0
                            val parsedDiscount = discountText.toIntOrNull() ?: 0
                            val finalProduct = Product(
                                id = productToEdit?.id ?: 0, // 0 for auto-generating PrimaryKey
                                titleEn = titleEn,
                                titleAm = titleAm,
                                descriptionEn = descEn,
                                descriptionAm = descAm,
                                price = parsedPrice,
                                rating = productToEdit?.rating ?: 5.0f,
                                categoryId = categoryId,
                                discountPercentage = parsedDiscount,
                                specificationsEn = productToEdit?.specificationsEn ?: listOf(),
                                specificationsAm = productToEdit?.specificationsAm ?: listOf(),
                                heroGradientIndex = productToEdit?.heroGradientIndex ?: 0,
                                imageUrl = imageUrl,
                                videoUrl = videoUrl
                            )
                            onSave(finalProduct)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold, contentColor = Color.Black),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = if (productToEdit != null) "SAVE AMENDMENTS" else "PUBLISH PRODUCT",
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}
