package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.PremiumRose
import com.example.ui.theme.PureWhite
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun SplashScreen(
    language: AppLanguage,
    onFinished: () -> Unit
) {
    val context = LocalContext.current
    
    // Core animation states
    var startAnimations by remember { mutableStateOf(false) }
    
    // 1. Scale animation with a beautiful premium overshoot bouncy spring
    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimations) 1.0f else 0.1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "premiumScale"
    )

    // 2. Rotation entry animation (Winds down from -180 degrees to 0 degrees)
    val introRotation by animateFloatAsState(
        targetValue = if (startAnimations) 0f else -180f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessVeryLow
        ),
        label = "introRotation"
    )

    // 3. Overall Fade-in animation
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimations) 1f else 0f,
        animationSpec = tween(1400, easing = EaseInOutCubic),
        label = "premiumAlpha"
    )
    
    // 4. Branding text Fade-in animation
    val textAlphaAnim by animateFloatAsState(
        targetValue = if (startAnimations) 1f else 0f,
        animationSpec = tween(1600, delayMillis = 600, easing = EaseOutQuart),
        label = "textAlpha"
    )

    // 5. Subtitle description Fade-in animation
    val subtitleAlphaAnim by animateFloatAsState(
        targetValue = if (startAnimations) 0.9f else 0f,
        animationSpec = tween(1600, delayMillis = 1100, easing = EaseOutQuart),
        label = "subtitleAlpha"
    )

    // 6. Infinite loop transitions for high-fidelity ambient movement
    val infiniteTransition = rememberInfiniteTransition(label = "premiumLoops")

    // Continuous 3D Y-axis elegant tilt loop
    val tiltYAnim by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tiltY"
    )

    // Continuous 3D X-axis elegant tilt loop
    val tiltXAnim by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tiltX"
    )

    // Gentle continuous vertical float (bobbing)
    val bobbingAnim by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "continuousBobbing"
    )

    // Continuous breathing scale pulse
    val breathingAnim by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1900, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "continuousBreathing"
    )

    // Background particle rotation
    val particleRotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(40000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particleRotation"
    )

    // Glowing heartbeat pulse for the background glow
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    LaunchedEffect(Unit) {
        startAnimations = true
        delay(3200) // Splash runs for ~3.2 seconds
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Change entire background to pristine white
        contentAlignment = Alignment.Center
    ) {
        // 1. Premium radial light-themed ambient glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            
            // Draw luxury gold soft radial glow in the center
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        LuxuryGold.copy(alpha = 0.12f * glowPulse),
                        Color.Transparent
                    ),
                    center = Offset(canvasWidth / 2f, canvasHeight / 2.2f),
                    radius = canvasWidth * 0.9f
                )
            )

            // Draw soft rose accent glow at the bottom right
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        PremiumRose.copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    center = Offset(canvasWidth * 0.8f, canvasHeight * 0.8f),
                    radius = canvasWidth * 0.7f
                )
            )
        }

        // 2. Decorative rotating starlight particle Canvas background
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .scale(scaleAnim)
                .alpha(alphaAnim)
        ) {
            val center = Offset(size.width / 2f, size.height / 2.2f)
            val random = Random(42) // Constant seed for deterministic visual beauty
            
            val numParticles = 40
            for (i in 0 until numParticles) {
                val distance = 180f + random.nextFloat() * 450f
                val sizeVal = 3f + random.nextFloat() * 7f
                val baseAngle = (i * (360f / numParticles)) + particleRotationAngle
                val rad = Math.toRadians(baseAngle.toDouble())
                val pX = center.x + (distance * kotlin.math.cos(rad)).toFloat()
                val pY = center.y + (distance * kotlin.math.sin(rad)).toFloat()
                
                // Draw luxury sparkling gold dust beautifully on white
                drawCircle(
                    color = LuxuryGold.copy(alpha = 0.25f + random.nextFloat() * 0.35f),
                    radius = sizeVal,
                    center = Offset(pX, pY)
                )
            }
        }

        // 3. Central branding and premium logo image
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(310.dp)
                    .scale(scaleAnim)
                    .alpha(alphaAnim),
                contentAlignment = Alignment.Center
            ) {
                // Background ambient soft blur circle behind the asset for luxury volume
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .blur(50.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    LuxuryGold.copy(alpha = 0.2f),
                                    PremiumRose.copy(alpha = 0.1f)
                                )
                            ),
                            shape = MaterialTheme.shapes.extraLarge
                        )
                )

                // High fidelity customized premium animated splash image
                Image(
                    painter = painterResource(id = R.drawable.ic_splash_logo),
                    contentDescription = "Teke Man Promotion Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .offset(y = bobbingAnim.dp)
                        .graphicsLayer {
                            rotationZ = introRotation
                            rotationY = tiltYAnim
                            rotationX = tiltXAnim
                            cameraDistance = 12 * density
                        }
                        .scale(breathingAnim),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Sub-greeting Welcome To
            Text(
                text = if (language == AppLanguage.ENGLISH) "WELCOME TO" else "እንኳን ወደ",
                color = LuxuryGold,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(textAlphaAnim)
                    .padding(bottom = 6.dp)
            )

            // Teke Promotion Master Branding Title in Slate-Dark for perfect readability
            Text(
                text = "TEKE MAN PROMOTION",
                color = Color(0xFF1E293B),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(textAlphaAnim)
                    .padding(bottom = 12.dp)
            )

            // Dynamic Accent Line with shimmering gradient
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(1.5.dp)
                    .alpha(textAlphaAnim)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                LuxuryGold,
                                PremiumRose,
                                LuxuryGold,
                                Color.Transparent
                            )
                        )
                    )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Elegant subtitle description
            Text(
                text = if (language == AppLanguage.ENGLISH) 
                    "THE ULTIMATE GIFT LUXURY" 
                else 
                    "እጅግ የላቀ የስጦታ ልምድ",
                color = Color(0xFF64748B),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(subtitleAlphaAnim)
            )
        }
    }
}
