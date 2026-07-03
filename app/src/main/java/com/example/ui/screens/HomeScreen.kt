package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.Product
import com.example.model.ProductRepository
import com.example.ui.components.GlassCard
import com.example.ui.components.ProductVisualThumbnail
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.LightGold
import com.example.ui.theme.PremiumRose
import com.example.ui.theme.PureWhite
import androidx.compose.foundation.text.BasicTextField
import com.example.viewmodel.TekeAppViewModel
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer

data class BannerSlide(
    val badge: String,
    val titleEn: String,
    val titleAm: String,
    val promo: String,
    val imageRes: Int,
    val isAlternateStyle: Boolean = false
)

@Composable
fun HomeScreen(
    viewModel: TekeAppViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var activeHeroIndex by remember { mutableStateOf(0) }
    
    // Auto slide hero carousel
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            activeHeroIndex = (activeHeroIndex + 1) % 3
        }
    }

    // Main layout container (vertical scrollable list of visual modules)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // 1. Top Bar Greeting & Profile Avatar
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (viewModel.currentLanguage == AppLanguage.ENGLISH) "EXCLUSIVE GIFT STUDIO" else "የቅንጦት ስጦታ ስቱዲዮ",
                    color = LuxuryGold,
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (viewModel.currentLanguage == AppLanguage.ENGLISH) "Teke Man" else "ተኬ ማን",
                        color = PureWhite,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Light,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (viewModel.currentLanguage == AppLanguage.ENGLISH) "Promotion" else "ፕሮሞሽን",
                        color = LightGold,
                        fontSize = 26.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Medium,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }

            // Sync & Crown Avatar Circle Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val coroutineScope = rememberCoroutineScope()
                
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .border(1.5.dp, LuxuryGold, shape = CircleShape)
                        .background(Color(0xFF1C1917), shape = CircleShape)
                        .clickable {
                            coroutineScope.launch {
                                viewModel.refreshCatalog()
                                Toast.makeText(
                                    context,
                                    if (viewModel.currentLanguage == AppLanguage.ENGLISH) "Catalog updated from cloud!" else "ካታሎግ ከደመናው ተዘምኗል!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Sync from cloud",
                        tint = LuxuryGold,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .border(1.5.dp, LuxuryGold, shape = CircleShape)
                        .background(Color(0xFF1C1917), shape = CircleShape)
                        .clickable {
                            viewModel.activeTab = "profile"
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Stars,
                        contentDescription = "VIP Member Profile",
                        tint = LuxuryGold,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 2. Luxury Glass search bar & Voice Trigger
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlassCard(
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                backgroundAlpha = 0.12f
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search icon",
                        tint = LuxuryGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (viewModel.searchQuery.isEmpty()) {
                            Text(
                                text = viewModel.translate("search_hint"),
                                color = PureWhite.copy(alpha = 0.45f),
                                fontSize = 14.sp
                            )
                        }
                        BasicTextField(
                            value = viewModel.searchQuery,
                            onValueChange = { 
                                viewModel.searchQuery = it
                                if (it.isNotEmpty()) {
                                    viewModel.activeTab = "categories"
                                    viewModel.selectedCategoryFilter = "all"
                                }
                            },
                            textStyle = LocalTextStyle.current.copy(color = PureWhite, fontSize = 14.sp),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                    if (viewModel.searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.searchQuery = "" },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "clear",
                                tint = PureWhite.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Voice Search Simulated Trigger Box
            GlassCard(
                modifier = Modifier
                    .size(52.dp)
                    .clickable {
                        Toast
                            .makeText(
                                context,
                                if (viewModel.currentLanguage == AppLanguage.ENGLISH) "Concierge listening..." else "የድምጽ ፍለጋ በመስማት ላይ...",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    },
                shape = RoundedCornerShape(16.dp),
                backgroundAlpha = 0.15f
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Simulated Voice Search",
                        tint = LuxuryGold,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Scrollable content area
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp) // buffer for floating bottom bar
        ) {
            // 3. Hero Carousel Banner Section
            Text(
                text = viewModel.translate("special_offers").uppercase(),
                color = LuxuryGold,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            val slides = listOf(
                BannerSlide(
                    badge = "VIP ACCESS",
                    titleEn = "Exclusive Royal Hampers",
                    titleAm = "ልዩ የነገስታት ጥቅሎች",
                    promo = "10% off code: TEKEGOLD",
                    imageRes = R.drawable.img_hero_banner
                ),
                BannerSlide(
                    badge = "LIMITED EDITION",
                    titleEn = "Priscilla Premium Chocolate",
                    titleAm = "ፕሪሲላ ፕሪሚየም ቸኮሌት",
                    promo = "Free Delivery code: CHOCOLUXE",
                    imageRes = R.drawable.img_gift_box_splash
                ),
                BannerSlide(
                    badge = "EXQUISITE ARTISTRY",
                    titleEn = "Bespoke Rose Collections",
                    titleAm = "ብጁ የሮዝ ስብስቦች",
                    promo = "Save 15% code: ROSES99",
                    imageRes = R.drawable.img_hero_banner,
                    isAlternateStyle = true
                )
            )

            val currentSlide = slides[activeHeroIndex]

            // 3D floating animation using InfiniteTransition
            val infiniteTransition = rememberInfiniteTransition(label = "banner3d")
            val floatRotationX by infiniteTransition.animateFloat(
                initialValue = -3f,
                targetValue = 3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2500, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "floatX"
            )
            val floatRotationY by infiniteTransition.animateFloat(
                initialValue = -5f,
                targetValue = 5f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "floatY"
            )

            // Dynamic rotation offset that spins 360 degrees when index changes
            var previousIndex by remember { mutableStateOf(0) }
            var spinAngle by remember { mutableStateOf(0f) }

            LaunchedEffect(activeHeroIndex) {
                if (activeHeroIndex != previousIndex) {
                    spinAngle += 360f
                    previousIndex = activeHeroIndex
                }
            }

            val animatedSpinAngle by animateFloatAsState(
                targetValue = spinAngle,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                label = "spinAngle"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .graphicsLayer {
                        rotationX = floatRotationX
                        rotationY = floatRotationY + animatedSpinAngle
                        cameraDistance = 14f * density
                        shadowElevation = 16f
                        clip = true
                        shape = RoundedCornerShape(24.dp)
                    }
                    .border(0.5.dp, LuxuryGold.copy(alpha = 0.3f), shape = RoundedCornerShape(24.dp))
            ) {
                // Background image loading
                Image(
                    painter = painterResource(id = currentSlide.imageRes),
                    contentDescription = "Teke Man Promotion Special Offers Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    colorFilter = if (currentSlide.isAlternateStyle) {
                        // Apply a luxury rose-gold tint to make the third slide look totally different!
                        ColorFilter.colorMatrix(
                            ColorMatrix(
                                floatArrayOf(
                                    1.1f, 0f, 0f, 0f, 20f,
                                    0f, 0.8f, 0f, 0f, 0f,
                                    0f, 0f, 0.7f, 0f, 0f,
                                    0f, 0f, 0f, 1f, 0f
                                )
                            )
                        )
                    } else null
                )

                // Dark elegant gold-vibe shader scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.85f),
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Animated Banner Copy Info
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(220.dp)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    GlassCard(
                        shape = RoundedCornerShape(6.dp),
                        backgroundAlpha = 0.4f,
                        borderAlpha = 0.5f,
                        glowColor = LuxuryGold
                    ) {
                        Text(
                            text = currentSlide.badge,
                            color = LuxuryGold,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (viewModel.currentLanguage == AppLanguage.ENGLISH) 
                            currentSlide.titleEn 
                        else 
                            currentSlide.titleAm,
                        color = PureWhite,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentSlide.promo,
                        color = LuxuryGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Small pager indicator circles bottom right
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(3) { i ->
                        Box(
                            modifier = Modifier
                                .size(if (activeHeroIndex == i) 16.dp else 6.dp, 6.dp)
                                .background(
                                    color = if (activeHeroIndex == i) LuxuryGold else PureWhite.copy(alpha = 0.4f),
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Horizontal Categories Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = viewModel.translate("categories").uppercase(),
                    color = LuxuryGold,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "See All",
                    color = PureWhite.copy(alpha = 0.5f),
                    fontSize = 12.sp,
                    modifier = Modifier.clickable {
                        viewModel.activeTab = "categories"
                        viewModel.selectedCategoryFilter = "all"
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(viewModel.categoriesList) { cat ->
                    val isSelected = viewModel.selectedCategoryFilter == cat.id
                    val scale by animateFloatAsState(if (isSelected) 1.05f else 1f)
                    
                    GlassCard(
                        modifier = Modifier
                            .scale(scale)
                            .clickable {
                                viewModel.selectedCategoryFilter = cat.id
                                viewModel.activeTab = "categories"
                            },
                        shape = RoundedCornerShape(14.dp),
                        backgroundAlpha = if (isSelected) 0.3f else 0.08f,
                        borderAlpha = if (isSelected) 0.6f else 0.18f,
                        glowRadius = if (isSelected) 4.dp else 0.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = cat.emoji, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = cat.getName(viewModel.currentLanguage),
                                color = if (isSelected) LuxuryGold else PureWhite.copy(alpha = 0.85f),
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Get filtered list of products based on selected category filter
            val activeCategory = viewModel.selectedCategoryFilter
            val filteredProducts = if (activeCategory == "all") {
                viewModel.productsList.filter { it.active }
            } else {
                viewModel.productsList.filter { it.categoryId == activeCategory && it.active }
            }

            if (filteredProducts.isEmpty()) {
                Spacer(modifier = Modifier.height(30.dp))
                Column(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "No products found",
                        tint = LuxuryGold.copy(alpha = 0.4f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (viewModel.currentLanguage == AppLanguage.ENGLISH) "No exclusive products found" else "ምንም ምርቶች አልተገኙም",
                        color = PureWhite,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (viewModel.currentLanguage == AppLanguage.ENGLISH) "Try selecting another elegant category above." else "እባክዎ ሌላ ምድብ ይምረጡ።",
                        color = PureWhite.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // 5. Product Grid (First 4 products in 2-column grid)
                Text(
                    text = if (viewModel.currentLanguage == AppLanguage.ENGLISH) "TOP COLLECTION (GRID)" else "ልዩ ምርጥ ስብስቦች (ግሪድ)",
                    color = LuxuryGold,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                val firstFour = filteredProducts.take(4)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val numRows = (firstFour.size + 1) / 2
                    for (row in 0 until numRows) {
                        val startIndex = row * 2
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            for (col in 0 until 2) {
                                val index = startIndex + col
                                if (index < firstFour.size) {
                                    val prod = firstFour[index]
                                    ProductGridCard(
                                        product = prod,
                                        viewModel = viewModel,
                                        onClick = { viewModel.selectedProductForDetail = prod },
                                        onFavoriteToggle = { viewModel.toggleFavorite(prod.id) },
                                        modifier = Modifier.weight(1f)
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                // 6. Horizontal Scroll Grid/List
                if (filteredProducts.size > 4) {
                    Spacer(modifier = Modifier.height(26.dp))
                    Text(
                        text = if (viewModel.currentLanguage == AppLanguage.ENGLISH) "FEATURED FINDS (SCROLL)" else "ወቅታዊ ምርጦች (በጎን ማሸብለያ)",
                        color = LuxuryGold,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    val scrollableItems = filteredProducts.drop(4)
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        items(scrollableItems) { prod ->
                            ProductGridCard(
                                product = prod,
                                viewModel = viewModel,
                                onClick = { viewModel.selectedProductForDetail = prod },
                                onFavoriteToggle = { viewModel.toggleFavorite(prod.id) },
                                modifier = Modifier.width(160.dp)
                            )
                        }
                    }
                }

                // 7. Remaining Products Grid (2-column grid at bottom)
                val remainingItems = filteredProducts.drop(6) // take remaining products after first 4 and scrollable items
                if (remainingItems.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(26.dp))
                    Text(
                        text = if (viewModel.currentLanguage == AppLanguage.ENGLISH) "MORE LUXURY DISCOVERIES" else "ተጨማሪ የቅንጦት ስጦታዎች",
                        color = LuxuryGold,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        val numRows = (remainingItems.size + 1) / 2
                        for (row in 0 until numRows) {
                            val startIndex = row * 2
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                for (col in 0 until 2) {
                                    val index = startIndex + col
                                    if (index < remainingItems.size) {
                                        val prod = remainingItems[index]
                                        ProductGridCard(
                                            product = prod,
                                            viewModel = viewModel,
                                            onClick = { viewModel.selectedProductForDetail = prod },
                                            onFavoriteToggle = { viewModel.toggleFavorite(prod.id) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ProductRowCard(
    product: Product,
    viewModel: TekeAppViewModel,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onAddToCart: () -> Unit
) {
    val isFav = viewModel.isFavorite(product.id)
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(product.id) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing)
        )
    }
    
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .graphicsLayer {
                alpha = animationProgress.value
                translationY = (1f - animationProgress.value) * 30f
            },
        shape = RoundedCornerShape(20.dp),
        backgroundAlpha = 0.06f,
        borderAlpha = 0.16f,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Left Side: 3D geometric thumb
            ProductVisualThumbnail(
                product = product,
                modifier = Modifier
                    .width(130.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
            )

            // Right Side: Details and microcta
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = product.getTitle(viewModel.currentLanguage),
                            color = PureWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        
                        // Heart Favorite Icon button
                        Icon(
                            imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "fav",
                            tint = if (isFav) PremiumRose else PureWhite.copy(alpha = 0.4f),
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { onFavoriteToggle() }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = product.getDescription(viewModel.currentLanguage),
                        color = PureWhite.copy(alpha = 0.55f),
                        fontSize = 11.sp,
                        maxLines = 2,
                        lineHeight = 15.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val discountedPrice = product.price * (1.0 - product.discountPercentage / 100.0)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${discountedPrice.toInt()} ${viewModel.translate("currency")}",
                            color = LuxuryGold,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (product.discountPercentage > 0) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${product.price.toInt()}",
                                color = PureWhite.copy(alpha = 0.35f),
                                fontSize = 11.sp,
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            )
                        }
                    }

                    // Add to basket micro button
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(LuxuryGold, shape = CircleShape)
                            .clickable { onAddToCart() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddShoppingCart,
                            contentDescription = "Quick add to cart",
                            tint = Color.Black,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductGridCard(
    product: Product,
    viewModel: TekeAppViewModel,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFav = viewModel.isFavorite(product.id)
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(product.id) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 650, easing = LinearOutSlowInEasing)
        )
    }
    
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
            .graphicsLayer {
                alpha = animationProgress.value
                translationY = (1f - animationProgress.value) * 35f
            },
        shape = RoundedCornerShape(20.dp),
        backgroundAlpha = 0.06f,
        borderAlpha = 0.16f,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Product 3D visual preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
            ) {
                ProductVisualThumbnail(
                    product = product,
                    modifier = Modifier.fillMaxSize()
                )
                
                // Heart Favorite Icon button on top-right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(28.dp)
                        .background(Color.Black.copy(alpha = 0.35f), shape = CircleShape)
                        .clickable { onFavoriteToggle() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "fav",
                        tint = if (isFav) PremiumRose else PureWhite.copy(alpha = 0.7f),
                        modifier = Modifier.size(15.dp)
                    )
                }

                // Discount tag if any
                if (product.discountPercentage > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(PremiumRose, shape = RoundedCornerShape(6.dp))
                    ) {
                        Text(
                            text = "-${product.discountPercentage}%",
                            color = PureWhite,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Description block
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = product.getTitle(viewModel.currentLanguage),
                        color = PureWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(2.dp))
                    
                    Text(
                        text = product.getDescription(viewModel.currentLanguage),
                        color = PureWhite.copy(alpha = 0.55f),
                        fontSize = 10.sp,
                        maxLines = 2,
                        lineHeight = 13.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val discountedPrice = product.price * (1.0 - product.discountPercentage / 100.0)
                    Column {
                        Text(
                            text = "${discountedPrice.toInt()} ${viewModel.translate("currency")}",
                            color = LuxuryGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (product.discountPercentage > 0) {
                            Text(
                                text = "${product.price.toInt()}",
                                color = PureWhite.copy(alpha = 0.35f),
                                fontSize = 9.sp,
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            )
                        }
                    }

                    // Add to cart micro-btn
                    val context = LocalContext.current
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(LuxuryGold, shape = CircleShape)
                            .clickable {
                                viewModel.addToCart(product.id)
                                Toast.makeText(context, viewModel.translate("added_to_cart"), Toast.LENGTH_SHORT).show()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddShoppingCart,
                            contentDescription = "Add to cart",
                            tint = Color.Black,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}
