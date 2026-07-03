package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.ObsidianBlack

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    borderAlpha: Float = 0.18f,
    backgroundAlpha: Float = 0.08f,
    glowColor: Color = LuxuryGold,
    glowRadius: Dp = 0.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background == ObsidianBlack
    
    // Choose backing colors based on active theme
    val bgColor = if (isDark) {
        Color(0xFF111113).copy(alpha = backgroundAlpha + 0.35f) // Deep glass
    } else {
        Color(0xFFFFFFFF).copy(alpha = backgroundAlpha + 0.65f) // Soft frost
    }

    val borderColor = if (isDark) {
        Color(0xFFF59E0B).copy(alpha = borderAlpha)
    } else {
        Color(0xFF111113).copy(alpha = borderAlpha * 0.7f)
    }

    val shadowModifier = if (glowRadius > 0.dp) {
        Modifier.shadow(
            elevation = glowRadius,
            shape = shape,
            clip = false,
            ambientColor = glowColor,
            spotColor = glowColor
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .then(shadowModifier)
            .clip(shape)
            .background(bgColor)
            .border(
                BorderStroke(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            borderColor,
                            borderColor.copy(alpha = borderAlpha * 0.3f),
                            borderColor,
                            borderColor.copy(alpha = borderAlpha * 0.1f)
                        )
                    )
                ),
                shape = shape
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true, color = if (isDark) LuxuryGold else Color.Black),
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            ),
        content = content
    )
}
