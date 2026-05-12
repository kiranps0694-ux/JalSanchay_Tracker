package com.mindmatrix.jalsanchay.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle

private val LightColors: ColorScheme = lightColorScheme(
    primary = Color(0xFF0040A1),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0056D2),
    onPrimaryContainer = Color(0xFFCCD8FF),
    secondary = Color(0xFF00696E),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF00F4FE),
    onSecondaryContainer = Color(0xFF006C71),
    tertiary = Color(0xFF006D37),
    onTertiary = Color.White,
    background = Color(0xFFF9F9FF),
    onBackground = Color(0xFF161C27),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF161C27),
    onSurfaceVariant = Color(0xFF424654)
)

private val DarkColors: ColorScheme = darkColorScheme(
    primary = Color(0xFFA5B4FC),
    onPrimary = Color(0xFF111827),
    primaryContainer = Color(0xFF312E81),
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = Color(0xFFC4B5FD),
    onSecondary = Color(0xFF111827),
    secondaryContainer = Color(0xFF4C1D95),
    onSecondaryContainer = Color(0xFFF5F3FF),
    tertiary = Color(0xFFF9A8D4),
    onTertiary = Color(0xFF111827),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    onSurfaceVariant = Color(0xFFCBD5E1)
)

val Indigo = Color(0xFF0040A1)
val Purple = Color(0xFF0056D2)
val Pink = Color(0xFF00F4FE)
val SoftLight = Color(0xFFF9F9FF)
val SoftSlate = Color(0xFFF1F3FF)

private val AppTypography = Typography().let {
    it.copy(
        displayLarge = it.displayLarge.copy(fontFamily = FontFamily.Serif, fontStyle = FontStyle.Normal),
        headlineLarge = it.headlineLarge.copy(fontFamily = FontFamily.Serif),
        headlineMedium = it.headlineMedium.copy(fontFamily = FontFamily.Serif),
        titleLarge = it.titleLarge.copy(fontFamily = FontFamily.SansSerif),
        bodyLarge = it.bodyLarge.copy(fontFamily = FontFamily.SansSerif),
        bodyMedium = it.bodyMedium.copy(fontFamily = FontFamily.SansSerif)
    )
}

@Composable
fun JalSanchayTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content
    )
}
