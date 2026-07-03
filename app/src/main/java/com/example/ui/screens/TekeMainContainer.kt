package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PremiumRose
import com.example.ui.theme.PureWhite
import com.example.viewmodel.TekeAppViewModel

@Composable
fun TekeMainContainer(
    viewModel: TekeAppViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Main scaffold with edge-to-edge safe area drawing
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.safeDrawing
        ) { paddingValues ->
            
            // 1. Core animated page transitions depending on tab selection
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AnimatedContent(
                    targetState = viewModel.activeTab,
                    transitionSpec = {
                        fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + 
                        slideInVertically(initialOffsetY = { 60 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) togetherWith
                        fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
                    },
                    label = "tab_transition",
                    modifier = Modifier.fillMaxSize()
                ) { tab ->
                    when (tab) {
                        "home" -> HomeScreen(viewModel = viewModel)
                        "categories" -> CategoriesScreen(viewModel = viewModel)
                        "favorites" -> FavoritesScreen(viewModel = viewModel)
                        "cart" -> CartScreen(viewModel = viewModel)
                        "profile" -> ProfileScreen(viewModel = viewModel)
                        else -> HomeScreen(viewModel = viewModel)
                    }
                }
            }
        }

        // 2. Custom Floating Glassmorphic Bottom Navigation Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 20.dp, vertical = 14.dp)
                .fillMaxWidth()
        ) {
            // High contrast blurred container to act as backdrop filter
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp),
                shape = RoundedCornerShape(26.dp),
                backgroundAlpha = 0.2f,
                borderAlpha = 0.22f,
                glowRadius = 12.dp,
                glowColor = LuxuryGold.copy(alpha = 0.15f)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val navItems = listOf(
                        NavigationItem("home", "Home", Icons.Filled.Home, Icons.Outlined.Home),
                        NavigationItem("categories", "Explore", Icons.Filled.GridView, Icons.Outlined.GridView),
                        NavigationItem("favorites", "Saved", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
                        NavigationItem("cart", "Cart", Icons.Filled.LocalMall, Icons.Outlined.LocalMall),
                        NavigationItem("profile", "Boutique", Icons.Filled.Stars, Icons.Outlined.Stars)
                    )

                    navItems.forEach { item ->
                        val isSelected = viewModel.activeTab == item.id
                        
                        // Icon spring scale animations
                        val scale by animateFloatAsState(
                            targetValue = if (isSelected) 1.25f else 1.0f,
                            animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium),
                            label = "nav_scale"
                        )

                        val alpha by animateFloatAsState(
                            targetValue = if (isSelected) 1.0f else 0.55f,
                            label = "nav_alpha"
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .clickable {
                                    viewModel.activeTab = item.id
                                    // reset search context if they exit categories
                                    if (item.id != "categories") {
                                        viewModel.searchQuery = ""
                                    }
                                }
                        ) {
                            Box(
                                modifier = Modifier
                                    .scale(scale)
                                    .alpha(alpha),
                                contentAlignment = Alignment.Center
                            ) {
                                // Background soft glowing indicator oval
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(width = 36.dp, height = 28.dp)
                                            .background(
                                                brush = Brush.radialGradient(
                                                    colors = listOf(LuxuryGold.copy(alpha = 0.25f), Color.Transparent)
                                                ),
                                                shape = CircleShape
                                            )
                                    )
                                }

                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label,
                                    tint = if (isSelected) LuxuryGold else PureWhite,
                                    modifier = Modifier.size(23.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            // Active dot
                            AnimatedVisibility(
                                visible = isSelected,
                                enter = scaleIn() + fadeIn(),
                                exit = scaleOut() + fadeOut()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .background(LuxuryGold, shape = CircleShape)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Immersive Product Details Overlay (Slide up bottom sheet animation)
        AnimatedVisibility(
            visible = viewModel.selectedProductForDetail != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            val activeProduct = viewModel.selectedProductForDetail
            if (activeProduct != null) {
                ProductDetailOverlay(
                    product = activeProduct,
                    viewModel = viewModel,
                    onDismiss = { viewModel.selectedProductForDetail = null }
                )
            }
        }

        // 4. Immersive Admin Console Overlay
        AnimatedVisibility(
            visible = viewModel.isAdminConsoleOpen,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            AdminConsoleOverlay(
                viewModel = viewModel,
                onDismiss = { viewModel.isAdminConsoleOpen = false }
            )
        }

    }
}

data class NavigationItem(
    val id: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)
