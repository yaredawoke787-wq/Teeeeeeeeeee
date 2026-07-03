package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ProductRepository
import com.example.ui.components.GlassCard
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PureWhite
import com.example.viewmodel.TekeAppViewModel

@Composable
fun FavoritesScreen(
    viewModel: TekeAppViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // Resolve full products from favorite IDs
    val favoriteProducts = viewModel.productsList.filter { 
        viewModel.favoriteProductIds.contains(it.id)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header Title
        Text(
            text = viewModel.translate("favorites").uppercase(),
            color = LuxuryGold,
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Your Saved Splendors",
            color = PureWhite,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (favoriteProducts.isEmpty()) {
            // Elegant Empty state
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    backgroundAlpha = 0.08f
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Big hollow favorite icon
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Empty Favorites",
                            tint = LuxuryGold.copy(alpha = 0.45f),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = viewModel.translate("empty_fav"),
                            color = PureWhite,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = viewModel.translate("empty_desc"),
                            color = PureWhite.copy(alpha = 0.6f),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // CTA to Browse
                        Button(
                            onClick = {
                                viewModel.activeTab = "home"
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LuxuryGold,
                                contentColor = ObsidianBlack
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "BROWSE DISCOVERIES",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        } else {
            // High-fidelity vertical grid of favorite items
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(favoriteProducts) { prod ->
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
