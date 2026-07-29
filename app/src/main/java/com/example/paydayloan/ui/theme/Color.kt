package com.example.paydayloan.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Premium City Bank Maroon Theme
val CityMaroon = Color(0xFFB11B21)
val CityMaroonDark = Color(0xFF800000)
val CityMaroonLight = Color(0xFFD32F2F)
val CityGold = Color(0xFFD4AF37)
val CityBackground = Color(0xFFF8F3F0)
val CitySurface = Color(0xFFFFFFFF)
val CityTextDark = Color(0xFF1A1A1A)
val CityTextGray = Color(0xFF666666)
val CitySuccess = Color(0xFF2E7D32)
val CityWarning = Color(0xFFF57C00)
val CityError = Color(0xFFC62828)

// Backward compatibility or legacy names if needed by other files (mapping to new theme)
val PrimaryBlue = CityMaroon
val SecondaryBlue = CityMaroonDark
val BackgroundBlue = CityBackground
val SurfaceWhite = CitySurface
val TextDark = CityTextDark
val TextGray = CityTextGray
val SuccessGreen = CitySuccess
val WarningOrange = CityWarning
val TabBackground = Color(0xFFF0F2F5)

// ---------------------------------------------------------------------------
// Theme-aware semantic colors.
//
// The screens paint surfaces and text from these tokens instead of fixed
// constants, so the whole app flips between light and dark from one place.
// Brand colors (CityMaroon, CityGold, CitySuccess/Warning/Error) stay constant
// in both modes — the maroon header and status accents read well on either.
// Dark palette is drawn from the City Touch reference: near-black canvas,
// slightly lifted charcoal cards, red brand accent, near-white text.
// ---------------------------------------------------------------------------
@Immutable
data class AppColors(
    val background: Color,      // page canvas behind cards
    val surface: Color,         // opaque cards, nav bar, sheets, dialogs, fields
    val surfaceVariant: Color,  // chips and subtle circular icon backgrounds
    val textPrimary: Color,     // headings and primary values (was CityTextDark)
    val textSecondary: Color,   // labels and captions (was CityTextGray)
    val divider: Color,         // hairline separators
    val glassBase: Color,       // base tint for translucent "frosted" cards
    val glassBorder: Color,     // border for translucent cards
    val isDark: Boolean
)

val LightAppColors = AppColors(
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFF0F0F0),
    textPrimary = CityTextDark,
    textSecondary = CityTextGray,
    divider = Color(0xFFEDEDED),
    glassBase = Color.White,
    glassBorder = Color.White.copy(alpha = 0.75f),
    isDark = false
)

val DarkAppColors = AppColors(
    background = Color(0xFF0D0D0D),
    surface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFF262626),
    textPrimary = Color(0xFFF5F5F5),
    textSecondary = Color(0xFF9E9E9E),
    divider = Color(0xFF2A2A2A),
    glassBase = Color(0xFF2A2A2A),
    glassBorder = Color.White.copy(alpha = 0.12f),
    isDark = true
)

val LocalAppColors = staticCompositionLocalOf { LightAppColors }

// Convenience accessor so screens can read `appColors.textPrimary` etc.
val appColors: AppColors
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current
