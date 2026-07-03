package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocalMall
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.Product
import com.example.model.ProductRepository
import com.example.ui.components.GlassCard
import com.example.ui.components.ProductVisualThumbnail
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PremiumRose
import com.example.ui.theme.PureWhite
import com.example.viewmodel.TekeAppViewModel

@Composable
fun ProductDetailOverlay(
    product: Product,
    viewModel: TekeAppViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var quantity by remember { mutableStateOf(1) }
    var rotationAngle by remember { mutableStateOf(0f) }
    var isSpecsExpanded by remember { mutableStateOf(true) }
    var isConfirmDialogOpen by remember { mutableStateOf(false) }

    // Floating animation
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack.copy(alpha = 0.96f))
    ) {
        // Main scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 110.dp) // padding for purchase bar
        ) {
            // 1. Immersive Header Visuals & 360 Viewer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
            ) {
                // Background Radial Glow
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(LuxuryGold.copy(alpha = 0.2f), Color.Transparent),
                            radius = size.width * 0.8f
                        )
                    )
                }

                // Interactive 360-degree rotation simulator
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                rotationAngle += dragAmount.x * 0.6f
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Overlapping visual cards rotating based on user horizontal swipe
                    Box(
                        modifier = Modifier
                            .size(280.dp)
                            .padding(16.dp)
                    ) {
                        ProductVisualThumbnail(
                            product = product,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(32.dp))
                        )
                    }

                    // Luxury 360 circular outline indicator
                    Canvas(modifier = Modifier.size(270.dp)) {
                        drawCircle(
                            color = LuxuryGold.copy(alpha = 0.12f),
                            style = Stroke(width = 3f)
                        )
                    }

                    // 360 badge
                    GlassCard(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 20.dp),
                        shape = RoundedCornerShape(12.dp),
                        backgroundAlpha = 0.3f
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = "360 rotation",
                                tint = LuxuryGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "360° VIEW (DRAG)",
                                color = PureWhite.copy(alpha = 0.8f),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            // 2. Core Info Details Block
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                // Category Chip & Discount
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val category = viewModel.categoriesList.find { it.id == product.categoryId }
                    Text(
                        text = (category?.getName(viewModel.currentLanguage) ?: "GIFT").uppercase(),
                        color = LuxuryGold,
                        style = MaterialTheme.typography.labelSmall
                    )

                    if (product.discountPercentage > 0) {
                        GlassCard(
                            shape = RoundedCornerShape(6.dp),
                            backgroundAlpha = 0.2f,
                            glowColor = PremiumRose
                        ) {
                            Text(
                                text = "-${product.discountPercentage}% OFF",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Product Localized Title
                Text(
                    text = product.getTitle(viewModel.currentLanguage),
                    color = PureWhite,
                    style = MaterialTheme.typography.displayMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Rating and Reviews Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.alpha(0.85f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star rating",
                                tint = if (index < product.rating.toInt()) LuxuryGold else Color.Gray.copy(alpha = 0.4f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${product.rating}",
                        color = LuxuryGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "|   48 ${viewModel.translate("reviews")}",
                        color = PureWhite.copy(alpha = 0.6f),
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Divider line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(LuxuryGold.copy(alpha = 0.15f))
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Pricing Info Box
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    val discountedPrice = product.price * (1.0 - product.discountPercentage / 100.0)
                    Text(
                        text = "${discountedPrice.toInt()} ${viewModel.translate("currency")}",
                        color = LuxuryGold,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                    if (product.discountPercentage > 0) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "${product.price.toInt()} ${viewModel.translate("currency")}",
                            color = PureWhite.copy(alpha = 0.4f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = product.getDescription(viewModel.currentLanguage),
                    color = PureWhite.copy(alpha = 0.75f),
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Premium Video Player
                com.example.ui.components.PremiumVideoPlayer(
                    videoUrl = product.videoUrl,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Service Cards (Gift wrapping + Delivery info)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlassCard(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        backgroundAlpha = 0.08f
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Icon(
                                imageVector = Icons.Default.Redeem,
                                contentDescription = "Gift Wrap",
                                tint = LuxuryGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = viewModel.translate("gift_options"),
                                color = PureWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = viewModel.translate("gift_wrapping"),
                                color = PureWhite.copy(alpha = 0.6f),
                                fontSize = 9.5.sp,
                                lineHeight = 13.sp
                            )
                        }
                    }

                    GlassCard(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        backgroundAlpha = 0.08f
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Icon(
                                imageVector = Icons.Default.LocalShipping,
                                contentDescription = "Express Shipping",
                                tint = LuxuryGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Express 2H Delivery",
                                color = PureWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = viewModel.translate("delivery_info"),
                                color = PureWhite.copy(alpha = 0.6f),
                                fontSize = 9.5.sp,
                                lineHeight = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // 4. Related Products section
                Text(
                    text = viewModel.translate("related_products").uppercase(),
                    color = LuxuryGold,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                val relatedList = viewModel.productsList.filter { 
                    it.id != product.id && (it.categoryId == product.categoryId || it.price < product.price + 2000)
                }.take(4)

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(relatedList) { item ->
                        GlassCard(
                            modifier = Modifier
                                .width(160.dp)
                                .clickable {
                                    viewModel.selectedProductForDetail = item
                                    // reset states
                                    quantity = 1
                                },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column {
                                ProductVisualThumbnail(
                                    product = item,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp)
                                )
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = item.getTitle(viewModel.currentLanguage),
                                        color = PureWhite,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${item.price.toInt()} ${viewModel.translate("currency")}",
                                        color = LuxuryGold,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        // 5. Sticky Floating Close button at top-left
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .safeDrawingPadding()
                .padding(16.dp)
                .size(42.dp)
                .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
                .border(1.dp, LuxuryGold.copy(alpha = 0.4f), shape = CircleShape)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Close Detail Screen",
                tint = PureWhite
            )
        }

        // 6. Sticky Floating Favorite button at top-right
        IconButton(
            onClick = { viewModel.toggleFavorite(product.id) },
            modifier = Modifier
                .safeDrawingPadding()
                .padding(16.dp)
                .size(42.dp)
                .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
                .border(1.dp, LuxuryGold.copy(alpha = 0.4f), shape = CircleShape)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = if (viewModel.isFavorite(product.id)) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Toggle Favorite",
                tint = if (viewModel.isFavorite(product.id)) PremiumRose else PureWhite
            )
        }

        // 7. Sticky Glassmorphic Purchase Bar at bottom
        GlassCard(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            backgroundAlpha = 0.25f,
            borderAlpha = 0.25f,
            glowRadius = 8.dp,
            glowColor = LuxuryGold.copy(alpha = 0.2f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quantity Selector
                GlassCard(
                    shape = RoundedCornerShape(16.dp),
                    backgroundAlpha = 0.15f
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "decrease", tint = PureWhite)
                        }
                        Text(
                            text = "$quantity",
                            color = PureWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        IconButton(
                            onClick = { quantity++ },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "increase", tint = PureWhite)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // CTA Button (Order Now / Add to Cart split depending on intent, we make it full Buy Now trigger!)
                Button(
                    onClick = { isConfirmDialogOpen = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LuxuryGold,
                        contentColor = ObsidianBlack
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocalMall,
                            contentDescription = "Buy Now",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = viewModel.translate("buy_now").uppercase(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }

        // 8. Call Confirmation Dialogue (Glassmorphic popup modal)
        if (isConfirmDialogOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.65f))
                    .blur(10.dp)
                    .pointerInput(Unit) {} // lock touches
            )

            androidx.compose.ui.window.Dialog(
                onDismissRequest = { isConfirmDialogOpen = false },
                properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
            ) {
                GlassCard(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    backgroundAlpha = 0.4f,
                    borderAlpha = 0.3f,
                    glowRadius = 16.dp,
                    glowColor = LuxuryGold
                ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Ring Icon in dynamic pulse
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(LuxuryGold.copy(alpha = 0.15f), shape = CircleShape)
                                    .border(1.dp, LuxuryGold.copy(alpha = 0.3f), shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhoneInTalk,
                                    contentDescription = "Phone call confirmation",
                                    tint = LuxuryGold,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = viewModel.translate("order_confirm_title"),
                                color = PureWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = viewModel.translate("order_confirm_desc"),
                                color = PureWhite.copy(alpha = 0.75f),
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 19.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Custom spec indicator
                            GlassCard(
                                shape = RoundedCornerShape(12.dp),
                                backgroundAlpha = 0.12f,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = product.getTitle(viewModel.currentLanguage),
                                        color = LuxuryGold,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Qty: $quantity",
                                            color = PureWhite.copy(alpha = 0.8f),
                                            fontSize = 11.sp
                                        )
                                        val discPrice = product.price * (1.0 - product.discountPercentage / 100.0)
                                        Text(
                                            text = "${(discPrice * quantity).toInt()} ${viewModel.translate("currency")}",
                                            color = PureWhite,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Action buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Cancel
                                OutlinedButton(
                                    onClick = { isConfirmDialogOpen = false },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PureWhite),
                                    border = BorderStroke(1.dp, PureWhite.copy(alpha = 0.3f)),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Text(
                                        text = viewModel.translate("cancel").uppercase(),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // Call
                                Button(
                                    onClick = {
                                        isConfirmDialogOpen = false
                                        viewModel.initiateOrderCall(context)
                                    },
                                    modifier = Modifier
                                        .weight(1.3f)
                                        .height(48.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = LuxuryGold,
                                        contentColor = ObsidianBlack
                                    ),
                                    shape = RoundedCornerShape(14.dp),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                                ) {
                                    Text(
                                        text = viewModel.translate("order_confirm_btn").uppercase(),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
