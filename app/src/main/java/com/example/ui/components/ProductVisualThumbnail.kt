package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import com.example.model.Product
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.PremiumRose

@Composable
fun ProductVisualThumbnail(
    product: Product,
    modifier: Modifier = Modifier
) {
    if (!product.imageUrl.isNullOrEmpty()) {
        SubcomposeAsyncImage(
            model = product.imageUrl,
            contentDescription = product.titleEn,
            contentScale = ContentScale.Crop,
            modifier = modifier,
            error = {
                FallbackCanvasThumbnail(product = product, modifier = Modifier.fillMaxSize())
            },
            loading = {
                FallbackCanvasThumbnail(product = product, modifier = Modifier.fillMaxSize())
            }
        )
    } else {
        FallbackCanvasThumbnail(product = product, modifier = modifier)
    }
}

@Composable
private fun FallbackCanvasThumbnail(
    product: Product,
    modifier: Modifier = Modifier
) {
    val gradientBrushes = listOf(
        Brush.linearGradient(colors = listOf(Color(0xFF1E1B4B), Color(0xFF31102F))), // Midnight Burgundy
        Brush.linearGradient(colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))), // Slate Carbon
        Brush.linearGradient(colors = listOf(Color(0xFF2E1065), Color(0xFF030712))), // Royal Purple
        Brush.linearGradient(colors = listOf(Color(0xFF022C22), Color(0xFF064E3B))), // Imperial Emerald
        Brush.linearGradient(colors = listOf(Color(0xFF451A03), Color(0xFF1C1917)))  // Cocoa Bronze
    )
    
    val activeBrush = gradientBrushes[product.heroGradientIndex % gradientBrushes.size]

    Box(
        modifier = modifier
            .background(activeBrush)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val centerX = width / 2f
            val centerY = height / 2f
            
            // Draw background subtle radial highlight
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.15f), Color.Transparent),
                    center = Offset(centerX, centerY * 0.9f),
                    radius = width * 0.6f
                )
            )

            when (product.categoryId) {
                "flowers" -> {
                    // Draw fresh luxury roses vector structure with gold leaves and glowing red center
                    rotate(15f) {
                        // Gold stem
                        val stemPath = Path().apply {
                            moveTo(centerX, centerY)
                            quadraticTo(centerX - 40f, centerY + 80f, centerX - 10f, centerY + 180f)
                        }
                        drawPath(
                            path = stemPath,
                            color = LuxuryGold.copy(alpha = 0.6f),
                            style = Stroke(width = 6f)
                        )
                        // Leaf 1
                        drawOval(
                            color = LuxuryGold,
                            topLeft = Offset(centerX - 40f, centerY + 90f),
                            size = Size(35f, 20f)
                        )
                    }

                    // Rose central nested petals (Gradient Rose red to gold)
                    val petalRadiusBase = width * 0.22f
                    for (i in 0 until 5) {
                        val angleOffset = i * 72f
                        rotate(angleOffset, pivot = Offset(centerX, centerY)) {
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(PremiumRose, PremiumRose.copy(alpha = 0.4f)),
                                    center = Offset(centerX - 15f, centerY - 15f),
                                    radius = petalRadiusBase
                                ),
                                radius = petalRadiusBase,
                                center = Offset(centerX - 20f, centerY)
                            )
                        }
                    }

                    // Golden central core crown
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFCD34D), PremiumRose),
                            center = Offset(centerX, centerY),
                            radius = petalRadiusBase * 0.6f
                        ),
                        radius = petalRadiusBase * 0.45f,
                        center = Offset(centerX, centerY)
                    )
                    
                    // Sparkling overlay
                    drawCircle(
                        color = Color.White.copy(alpha = 0.8f),
                        radius = 8f,
                        center = Offset(centerX - 30f, centerY - 30f)
                    )
                }
                
                "chocolate" -> {
                    // Draw premium chocolate truffles & caramel drop
                    val boxW = width * 0.35f
                    val boxH = width * 0.35f
                    
                    // Shadow
                    drawRoundRect(
                        color = Color.Black.copy(alpha = 0.5f),
                        topLeft = Offset(centerX - boxW/2f + 15f, centerY - boxH/2f + 25f),
                        size = Size(boxW, boxH),
                        cornerRadius = CornerRadius(20f, 20f)
                    )
                    
                    // Chocolate Base Block 1
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF3B2314), Color(0xFF1D0E07))
                        ),
                        topLeft = Offset(centerX - boxW/2f, centerY - boxH/2f),
                        size = Size(boxW, boxH),
                        cornerRadius = CornerRadius(20f, 20f)
                    )

                    // Chocolate Drizzling Lines (Gold)
                    val drizzle = Path().apply {
                        moveTo(centerX - boxW/2f + 10f, centerY - boxH/2f + 15f)
                        quadraticTo(centerX, centerY - 10f, centerX + boxW/2f - 10f, centerY - boxH/2f + 25f)
                        moveTo(centerX - boxW/2f + 15f, centerY + 10f)
                        quadraticTo(centerX, centerY + 25f, centerX + boxW/2f - 15f, centerY + 5f)
                    }
                    drawPath(
                        path = drizzle,
                        color = LuxuryGold,
                        style = Stroke(width = 5f)
                    )

                    // Secondary Truffle next to it
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF653B19), Color(0xFF2C1605))
                        ),
                        radius = width * 0.12f,
                        center = Offset(centerX + 35f, centerY + 45f)
                    )

                    // Gold shimmer flake on the truffle
                    drawCircle(
                        color = LuxuryGold,
                        radius = 10f,
                        center = Offset(centerX + 25f, centerY + 35f)
                    )
                }

                "wedding" -> {
                    // Draw two elegant gold & platinum interlocking wedding bands
                    val ringRadius = width * 0.18f
                    val strokeW = 12f

                    // Ring 1: Luxury Gold
                    drawCircle(
                        color = LuxuryGold,
                        radius = ringRadius,
                        center = Offset(centerX - 25f, centerY),
                        style = Stroke(width = strokeW)
                    )
                    // Inner reflection ring 1
                    drawCircle(
                        color = Color.White.copy(alpha = 0.7f),
                        radius = ringRadius - 3f,
                        center = Offset(centerX - 28f, centerY - 3f),
                        style = Stroke(width = 3f)
                    )

                    // Ring 2: Platinum Silver
                    drawCircle(
                        color = Color(0xFFE2E8F0),
                        radius = ringRadius,
                        center = Offset(centerX + 25f, centerY - 15f),
                        style = Stroke(width = strokeW)
                    )
                    // Inner reflection ring 2
                    drawCircle(
                        color = Color.White.copy(alpha = 0.9f),
                        radius = ringRadius - 3f,
                        center = Offset(centerX + 22f, centerY - 18f),
                        style = Stroke(width = 3f)
                    )

                    // Giant Sparkle on Ring intersect
                    val sparklePath = Path().apply {
                        moveTo(centerX + 25f, centerY - ringRadius - 25f)
                        lineTo(centerX + 30f, centerY - ringRadius - 15f)
                        lineTo(centerX + 40f, centerY - ringRadius - 15f)
                        lineTo(centerX + 32f, centerY - ringRadius - 10f)
                        lineTo(centerX + 35f, centerY)
                        lineTo(centerX + 25f, centerY - ringRadius - 5f)
                        lineTo(centerX + 15f, centerY)
                        lineTo(centerX + 18f, centerY - ringRadius - 10f)
                        lineTo(centerX + 10f, centerY - ringRadius - 15f)
                        lineTo(centerX + 20f, centerY - ringRadius - 15f)
                        close()
                    }
                    drawPath(
                        path = sparklePath,
                        color = Color(0xFFFEF08A)
                    )
                }

                "corporate" -> {
                    // Draw executive stationery pen & premium desk portfolio setup
                    val docW = width * 0.45f
                    val docH = width * 0.5f
                    
                    // Portfolio Leather Base
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF27272A), Color(0xFF09090B))
                        ),
                        topLeft = Offset(centerX - docW/2f, centerY - docH/2f),
                        size = Size(docW, docH),
                        cornerRadius = CornerRadius(15f, 15f)
                    )

                    // Premium Gold logo marker on the portfolio
                    drawRoundRect(
                        color = LuxuryGold,
                        topLeft = Offset(centerX - 15f, centerY - docH/2f + 20f),
                        size = Size(30f, 10f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )

                    // Executive Roller Pen overlay (angled)
                    rotate(-35f, pivot = Offset(centerX, centerY)) {
                        // Pen Body
                        drawRoundRect(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFE2E8F0), Color(0xFF475569))
                            ),
                            topLeft = Offset(centerX - 8f, centerY - 100f),
                            size = Size(16f, 200f),
                            cornerRadius = CornerRadius(8f, 8f)
                        )
                        // Gold clip
                        drawRoundRect(
                            color = LuxuryGold,
                            topLeft = Offset(centerX - 4f, centerY - 80f),
                            size = Size(8f, 60f),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                        // Gold center ring
                        drawRect(
                            color = LuxuryGold,
                            topLeft = Offset(centerX - 8f, centerY),
                            size = Size(16f, 10f)
                        )
                    }
                }

                "customized" -> {
                    // Ribbon bow vector on pristine obsidian background
                    val ribbonWidth = width * 0.14f
                    
                    // Ribbon strap vertical
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(LuxuryGold, LuxuryGold.copy(alpha = 0.5f))
                        ),
                        topLeft = Offset(centerX - ribbonWidth/2f, 0f),
                        size = Size(ribbonWidth, height)
                    )
                    
                    // Ribbon strap horizontal
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(LuxuryGold, LuxuryGold.copy(alpha = 0.5f))
                        ),
                        topLeft = Offset(0f, centerY - ribbonWidth/2f),
                        size = Size(width, ribbonWidth)
                    )

                    // Custom laser engraved plate
                    val plateW = width * 0.42f
                    val plateH = width * 0.22f
                    drawRoundRect(
                        color = Color(0xFF1E293B),
                        topLeft = Offset(centerX - plateW/2f, centerY - plateH/2f),
                        size = Size(plateW, plateH),
                        cornerRadius = CornerRadius(12f, 12f),
                        style = Stroke(width = 4f)
                    )
                    
                    // Gold text simulation lines on the plaque
                    drawRoundRect(
                        color = LuxuryGold,
                        topLeft = Offset(centerX - plateW/3f, centerY - 10f),
                        size = Size(plateW * 0.6f, 6f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                    drawRoundRect(
                        color = LuxuryGold,
                        topLeft = Offset(centerX - plateW/4f, centerY + 10f),
                        size = Size(plateW * 0.4f, 6f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                }

                else -> {
                    // Default Anniversary & Birthday: Majestic Floating Gift box
                    val boxSize = width * 0.32f
                    
                    // Gift Box Base (Slate/Black gradient)
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF3F3F46), Color(0xFF18181B))
                        ),
                        topLeft = Offset(centerX - boxSize/2f, centerY - boxSize/3f),
                        size = Size(boxSize, boxSize),
                        cornerRadius = CornerRadius(16f, 16f)
                    )

                    // Gift Box Lid (overlapping slightly wider)
                    val lidW = boxSize * 1.1f
                    val lidH = boxSize * 0.25f
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF52525B), Color(0xFF27272A))
                        ),
                        topLeft = Offset(centerX - lidW/2f, centerY - boxSize/3f - lidH + 4f),
                        size = Size(lidW, lidH),
                        cornerRadius = CornerRadius(8f, 8f)
                    )

                    // Velvet Red or Golden Ribbon tied around the box
                    val ribbonW = boxSize * 0.18f
                    drawRect(
                        color = LuxuryGold,
                        topLeft = Offset(centerX - ribbonW/2f, centerY - boxSize/3f - lidH + 4f),
                        size = Size(ribbonW, boxSize + lidH)
                    )

                    // Soft Ribbon loop ears (2 intersecting ovals at the top)
                    drawOval(
                        color = LuxuryGold,
                        topLeft = Offset(centerX - ribbonW - 10f, centerY - boxSize/3f - lidH - 22f),
                        size = Size(ribbonW * 1.5f, 32f)
                    )
                    drawOval(
                        color = LuxuryGold,
                        topLeft = Offset(centerX + 5f, centerY - boxSize/3f - lidH - 22f),
                        size = Size(ribbonW * 1.5f, 32f)
                    )

                    // Sparkle stars floating
                    drawCircle(
                        color = Color.White.copy(alpha = 0.8f),
                        radius = 6f,
                        center = Offset(centerX - boxSize * 0.7f, centerY - 20f)
                    )
                    drawCircle(
                        color = LuxuryGold,
                        radius = 8f,
                        center = Offset(centerX + boxSize * 0.7f, centerY + 30f)
                    )
                }
            }
        }
    }
}
