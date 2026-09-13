package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = BlackBackground,
    primaryContainer = GoldContainer,
    onPrimaryContainer = OnGoldContainer,
    secondary = GoldVariant,
    onSecondary = BlackBackground,
    secondaryContainer = BlackSurfaceVariant,
    onSecondaryContainer = GoldVariant,
    tertiary = GoldDark,
    onTertiary = WhitePure,
    background = BlackBackground,
    onBackground = WhitePure,
    surface = BlackSurface,
    onSurface = WhitePure,
    surfaceVariant = BlackSurfaceVariant,
    onSurfaceVariant = GrayText,
    outline = BlackSurfaceBorder,
    outlineVariant = BlackSurfaceBorder,
    error = ErrorRed,
    onError = BlackBackground
)

// Elegant Light Scheme for light mode compatibility while retaining black & gold accents
private val LightColorScheme = lightColorScheme(
    primary = GoldDark,
    onPrimary = WhitePure,
    primaryContainer = Color(0xFFFFF3D4),
    onPrimaryContainer = Color(0xFF423200),
    secondary = Color(0xFF1E1E24),
    onSecondary = WhitePure,
    secondaryContainer = Color(0xFFF0EFEA),
    onSecondaryContainer = Color(0xFF1E1E24),
    tertiary = GoldPrimary,
    onTertiary = BlackBackground,
    background = Color(0xFFF9F9FB),
    onBackground = Color(0xFF121214),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF121214),
    surfaceVariant = Color(0xFFF1F0F5),
    onSurfaceVariant = Color(0xFF53525A),
    outline = Color(0xFFD8D7DE),
    outlineVariant = Color(0xFFE5E4EB),
    error = ErrorRed,
    onError = WhitePure
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek black and gold as requested
    dynamicColor: Boolean = false, // Keep consistent branded barbershop aesthetic
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
