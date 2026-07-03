package com.example.ui.components

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.LuxuryGold
import com.example.ui.theme.PureWhite
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun PremiumVideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    if (videoUrl.isBlank()) {
        // Fallback banner if video URL is blank
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1E293B)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "TEKE PROMO VIDEO",
                    color = LuxuryGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "No custom video loaded",
                    color = PureWhite.copy(alpha = 0.5f),
                    fontSize = 10.sp
                )
            }
        }
        return
    }

    var isPlaying by remember { mutableStateOf(false) }
    var duration by remember { mutableStateOf(0f) }
    var currentPosition by remember { mutableStateOf(0f) }
    var isPreparing by remember { mutableStateOf(true) }
    var isMuted by remember { mutableStateOf(false) }
    var videoViewRef by remember { mutableStateOf<VideoView?>(null) }

    // Helper to format milliseconds into mm:ss
    fun formatTime(ms: Float): String {
        val totalSeconds = (ms / 1000).toInt()
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.US, "%02d:%02d", minutes, seconds)
    }

    // Coroutine to poll the video position
    LaunchedEffect(isPlaying, isPreparing) {
        if (isPlaying && !isPreparing) {
            while (isPlaying) {
                videoViewRef?.let {
                    currentPosition = it.currentPosition.toFloat()
                }
                delay(250)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // The Native VideoView
        AndroidView(
            factory = { ctx ->
                VideoView(ctx).apply {
                    setVideoURI(Uri.parse(videoUrl))
                    setOnPreparedListener { mp ->
                        isPreparing = false
                        duration = mp.duration.toFloat()
                        mp.isLooping = true
                        start()
                        isPlaying = true
                        if (isMuted) {
                            mp.setVolume(0f, 0f)
                        } else {
                            mp.setVolume(1f, 1f)
                        }
                    }
                    setOnErrorListener { _, _, _ ->
                        isPreparing = false
                        false
                    }
                }
            },
            update = { view ->
                videoViewRef = view
            },
            modifier = Modifier.fillMaxSize()
        )

        // Loading Indicator
        if (isPreparing) {
            CircularProgressIndicator(
                color = LuxuryGold,
                modifier = Modifier.size(36.dp)
            )
        }

        // Control Panel overlay (visible when not preparing)
        if (!isPreparing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f))
            ) {
                // Bottom control overlay
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.55f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    // Progress Slider
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Slider(
                            value = currentPosition.coerceIn(0f, duration),
                            onValueChange = { newValue ->
                                currentPosition = newValue
                                videoViewRef?.seekTo(newValue.toInt())
                            },
                            valueRange = 0f..duration,
                            colors = SliderDefaults.colors(
                                thumbColor = LuxuryGold,
                                activeTrackColor = LuxuryGold,
                                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Play/Pause, Mute and Timestamps Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Play/Pause Button
                            IconButton(
                                onClick = {
                                    videoViewRef?.let {
                                        if (it.isPlaying) {
                                            it.pause()
                                            isPlaying = false
                                        } else {
                                            it.start()
                                            isPlaying = true
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(LuxuryGold, shape = CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                    contentDescription = "Play/Pause",
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        // Time stamp display (e.g., 00:15 / 02:45)
                        Text(
                            text = "${formatTime(currentPosition)} / ${formatTime(duration)}",
                            color = PureWhite,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
