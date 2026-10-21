package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CineDarkColorScheme = darkColorScheme(
    primary = NetflixRed,
    onPrimary = TextPrimary,
    primaryContainer = NetflixRedHover,
    onPrimaryContainer = TextPrimary,
    secondary = LanguageGreen,
    onSecondary = CineBackground,
    secondaryContainer = CineSurfaceHighlight,
    onSecondaryContainer = LanguageGreen,
    tertiary = GoldRating,
    onTertiary = CineBackground,
    background = CineBackground,
    onBackground = TextPrimary,
    surface = CineSurface,
    onSurface = TextPrimary,
    surfaceVariant = CineSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = BorderDark,
    outlineVariant = CineSurfaceHighlight
)

@Composable
fun CineZenixTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CineDarkColorScheme,
        typography = Typography,
        content = content
    )
}
