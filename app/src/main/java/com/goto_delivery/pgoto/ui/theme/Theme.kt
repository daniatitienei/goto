package com.goto_delivery.pgoto.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun GotoTheme(isDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        content = content,
        typography = GotoTypography,
        colorScheme = if (isDarkTheme) DarkThemeColors else LightThemeColors,
    )
}

private val LightThemeColors = lightColorScheme(
    primary = Lime80,
    onPrimary = Color.White,
    primaryContainer = LightGrey90,
    onPrimaryContainer = Color.Black,
    secondary = Lime80,
    onSecondary = Color.White,
    surface = Color.White,
    onSurface = Lime80,
    onBackground = Lime80,
    tertiary = Magenta80,
    onTertiary = Magenta20,
    tertiaryContainer = Magenta30,
    error = Red80,
    errorContainer = Red20,
)

private val DarkThemeColors = darkColorScheme(
    primary = Lime80,
    onPrimary = DarkGreen90,
    primaryContainer = DarkGreen20,
    onPrimaryContainer = Color.White,
    secondary = Lime80,
    onSecondary = DarkGreen10,
    background = DarkGreen90,
    onBackground = Lime80,
    onSurface = Lime80,
    surface = DarkGreen90,
    tertiary = Magenta80,
    onTertiary = Magenta20,
    tertiaryContainer = Magenta30,
    error = Red80,
    errorContainer = Red20,
)