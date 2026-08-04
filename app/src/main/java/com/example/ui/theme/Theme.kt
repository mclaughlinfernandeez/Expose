package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = SovereignGoldPrimary,
    onPrimary = Color.Black,
    primaryContainer = SovereignGoldDark,
    onPrimaryContainer = Color.White,
    secondary = AnalyticsCyan,
    onSecondary = Color.Black,
    tertiary = StatSuccessGreen,
    background = DarkCanvasBg,
    onBackground = Color(0xFFF3F4F6),
    surface = DarkSurfaceCard,
    onSurface = Color(0xFFF9FAFB),
    surfaceVariant = DarkSurfaceCardVariant,
    onSurfaceVariant = Color(0xFFD1D5DB),
    outline = DarkBorderOutline
  )

private val LightColorScheme =
  lightColorScheme(
    primary = SovereignGoldDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFEF3C7),
    onPrimaryContainer = Color(0xFF92400E),
    secondary = AnalyticsCyanDark,
    onSecondary = Color.White,
    tertiary = StatSuccessGreen,
    background = LightCanvasBg,
    onBackground = Color(0xFF0F172A),
    surface = LightSurfaceCard,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = LightSurfaceCardVariant,
    onSurfaceVariant = Color(0xFF475569),
    outline = LightBorderOutline
  )

@Composable
fun SovereignAnalyticsTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Preserve sovereign theme identity
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

