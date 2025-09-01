package com.amartya.aquysa.ui.theme

import android.app.Activity
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
    primary = Color(0xFF1A94E5), // Your primary blue
    onPrimary = Color.White,     // White text on primary blue
    secondary = Color(0xFF64B5F6), // Lighter shade of blue
    onSecondary = Color.Black,   // Black text on lighter blue
    tertiary = Color(0xFFA7D9FF), // Even lighter shade of blue
    onTertiary = Color.Black,    // Black text on very light blue
    background = Color(0xFF121212), // Very dark gray for background
    onBackground = Color.White,  // White text on dark background
    surface = Color(0xFF121212),     // Very dark gray for surface
    onSurface = Color.White,     // White text on dark surface
    // Add other colors as needed for your dark theme
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1A94E5), // Your primary blue
    onPrimary = Color.White,     // White text on primary blue
    secondary = Color(0xFF42A5F5), // Lighter shade of blue
    onSecondary = Color.White,   // White text on secondary blue
    tertiary = Color(0xFF90CAF9), // Even lighter shade of blue
    onTertiary = Color.Black,    // Black text on very light blue
    background = Color.White,    // White background
    onBackground = Color.Black,  // Black text on white background
    surface = Color.White,       // White surface
    onSurface = Color.Black,     // Black text on white surface
    // Add other colors as needed for your light theme
)

@Composable
fun AquysaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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