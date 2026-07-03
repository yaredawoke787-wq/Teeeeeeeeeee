package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Product
import com.example.model.ProductRepository
import com.example.ui.components.GlassCard
import com.example.ui.components.ProductVisualThumbnail
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.PremiumRose
import com.example.ui.theme.PureWhite
import com.example.viewmodel.TekeAppViewModel

@Composable
fun CategoriesScreen(
    viewModel: TekeAppViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activeCat = viewModel.categoriesList.find { it.id == viewModel.selectedCategoryFilter } ?: viewModel.categoriesList[0]

    // Filter products
    val filteredProducts = viewModel.productsList.filter { prod ->
        val matchesActive = prod.active
        val matchesCategory = viewModel.selectedCategoryFilter == "all" || prod.categoryId == viewModel.selectedCategoryFilter
        val matchesSearch = viewModel.searchQuery.isEmpty() || 
                prod.titleEn.contains(viewModel.searchQuery, ignoreCase = true) ||
                prod.titleAm.contains(viewModel.searchQuery, ignoreCase = true) ||
                prod.descriptionEn.contains(viewModel.searchQuery, ignoreCase = true) ||
                prod.descriptionAm.contains(viewModel.searchQuery, ignoreCase = true)
        
        matchesActive && matchesCategory && matchesSearch
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Screen Master Title
        Text(
            text = viewModel.translate("categories").uppercase(),
            color = LuxuryGold,
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = if (viewModel.searchQuery.isNotEmpty()) "Search Results" else activeCat.getName(viewModel.currentLanguage),
            color = PureWhite,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Horizontal Category Chips scroll selector
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(viewModel.categoriesList) { cat ->
                val isSelected = viewModel.selectedCategoryFilter == cat.id
                val scale by animateFloatAsState(if (isSelected) 1.05f else 1f, label = "chip_scale")

                GlassCard(
                    modifier = Modifier
                        .scale(scale)
                        .clickable {
                            viewModel.selectedCategoryFilter = cat.id
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

        Spacer(modifier = Modifier.height(16.dp))

        // Category localized subtitle description
        if (viewModel.searchQuery.isEmpty()) {
            Text(
                text = activeCat.getDescription(viewModel.currentLanguage),
                color = PureWhite.copy(alpha = 0.65f),
                fontSize = 13.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Staggered Vertical Grid of Filtered Products
        if (filteredProducts.isEmpty()) {
            // Luxury Empty State layout
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    shape = RoundedCornerShape(24.dp),
                    backgroundAlpha = 0.1f
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SentimentVeryDissatisfied,
                            contentDescription = "Not found",
                            tint = LuxuryGold.copy(alpha = 0.6f),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "No Exquisite Items Found",
                            color = PureWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Try clearing your search query or selecting another royal category.",
                            color = PureWhite.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 80.dp), // buffer for floating nav
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredProducts) { prod ->
                    ProductGridCard(
                        product = prod,
                        viewModel = viewModel,
                        onClick = { viewModel.selectedProductForDetail = prod },
                        onFavoriteToggle = { viewModel.toggleFavorite(prod.id) },
                        onAddToCart = {
                            viewModel.addToCart(prod.id)
                            Toast.makeText(context, viewModel.translate("added_to_cart"), Toast.LENGTH_SHORT).show()
                        }
                    )
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
    onAddToCart: () -> Unit
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
        modifier = Modifier
            .fillMaxWidth()
            .height(235.dp)
            .graphicsLayer {
                alpha = animationProgress.value
                translationY = (1f - animationProgress.value) * 35f
            },
        shape = RoundedCornerShape(22.dp),
        backgroundAlpha = 0.05f,
        borderAlpha = 0.14f,
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Visual top thumbnail
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                ProductVisualThumbnail(
                    product = product,
                    modifier = Modifier.fillMaxSize()
                )

                // Favorite float circular button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(32.dp)
                        .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                        .clickable { onFavoriteToggle() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "fav",
                        tint = if (isFav) PremiumRose else PureWhite,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Floating Discount badge if any
                if (product.discountPercentage > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                            .background(PremiumRose, shape = RoundedCornerShape(4.dp))
                    ) {
                        Text(
                            text = "-${product.discountPercentage}%",
                            color = Color.White,
                            fontSize = 8.5.sp,
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
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = product.getDescription(viewModel.currentLanguage),
                        color = PureWhite.copy(alpha = 0.5f),
                        fontSize = 10.sp,
                        maxLines = 1,
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
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (product.discountPercentage > 0) {
                            Text(
                                text = "${product.price.toInt()} ${viewModel.translate("currency")}",
                                color = PureWhite.copy(alpha = 0.3f),
                                fontSize = 9.5.sp,
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            )
                        }
                    }

                    // Plus cart button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(LuxuryGold, shape = CircleShape)
                            .clickable { onAddToCart() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddShoppingCart,
                            contentDescription = "Add",
                            tint = Color.Black,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
