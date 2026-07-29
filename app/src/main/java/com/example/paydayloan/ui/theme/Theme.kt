package com.example.paydayloan.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// User-selectable appearance. Held in a process-wide holder so the toggle in
// Settings updates the whole app instantly without threading state through
// every navigation destination.
enum class ThemeMode { SYSTEM, LIGHT, DARK }

object ThemeController {
    var mode by mutableStateOf(ThemeMode.SYSTEM)
}

private val DarkColorScheme = darkColorScheme(
    primary = CityMaroon,
    secondary = CityGold,
    tertiary = CityMaroonLight,
    background = Color(0xFF0D0D0D),
    surface = Color(0xFF1A1A1A),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color(0xFFF5F5F5),
    onSurface = Color(0xFFF5F5F5),
    error = CityError,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = CityMaroon,
    secondary = CityGold,
    tertiary = CityMaroonLight,
    background = CityBackground,
    surface = CitySurface,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = CityTextDark,
    onSurface = CityTextDark,
    error = CityError,
    onError = Color.White
)

@Composable
fun paydayloanTheme(
    // Resolves the effective dark state from the user's appearance choice,
    // falling back to the system setting when SYSTEM is selected.
    darkTheme: Boolean = when (ThemeController.mode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    },
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Set to false to maintain brand identity
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

    val appColors = if (darkTheme) DarkAppColors else LightAppColors

    // Keep the status bar icons legible: light icons in dark mode, dark in light.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}