package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NoxCrashColorScheme = darkColorScheme(
    primary = ColorMining,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onPrimary = DarkBackground,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = BorderColor
)

@Composable
fun NoxCrashTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NoxCrashColorScheme,
        typography = Typography,
        content = content
    )
}
