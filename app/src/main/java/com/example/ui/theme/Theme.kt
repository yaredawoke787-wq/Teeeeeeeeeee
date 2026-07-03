package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = LuxuryGold,
    secondary = LightGold,
    tertiary = PremiumRose,
    background = ObsidianBlack,
    surface = DeepSlate,
    onPrimary = ObsidianBlack,
    onSecondary = ObsidianBlack,
    onTertiary = PureWhite,
    onBackground = SilkWhite,
    onSurface = SilkWhite
  )

private val LightColorScheme =
  lightColorScheme(
    primary = LuxuryGold,
    secondary = WarmBronze,
    tertiary = PremiumRose,
    background = SilkWhite,
    surface = PureWhite,
    onPrimary = PureWhite,
    onSecondary = PureWhite,
    onTertiary = TextMainDark,
    onBackground = TextMainDark,
    onSurface = TextMainDark
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
