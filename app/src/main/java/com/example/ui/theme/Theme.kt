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

private val DarkColorScheme = darkColorScheme(
    primary = KrishiGreenDark,
    onPrimary = Color(0xFF003915),
    primaryContainer = KrishiGreenDarkContainer,
    onPrimaryContainer = Color(0xFFA6F5A6),
    secondary = KrishiHarvestGoldDark,
    onSecondary = Color(0xFF4A2800),
    secondaryContainer = Color(0xFF6B3C00),
    onSecondaryContainer = Color(0xFFFFDDB8),
    tertiary = KrishiSkyBlue,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = KrishiGreenLight,
    onPrimary = Color.White,
    primaryContainer = KrishiGreenLightContainer,
    onPrimaryContainer = Color(0xFF00210A),
    secondary = KrishiHarvestGold,
    onSecondary = Color.White,
    secondaryContainer = KrishiHarvestGoldContainer,
    onSecondaryContainer = Color(0xFF2C1600),
    tertiary = KrishiSkyBlue,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    outline = LightOutline
)

@Composable
fun KrishiMitraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep themed agriculture colors consistent
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
