package com.goto_deliveryl.pgoto.ui.theme

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
        colorScheme = if (isDarkTheme) darkColorScheme else lightColorScheme,
    )
}

private val lightColorScheme = lightColorScheme(
    primary = Lime900,
    onPrimary = Color.Black,
    primaryContainer = LightGrey900,
    onPrimaryContainer = Color.Black,
    secondary = Lime700,
    onSecondary = Color.White,
    onSurface = LightGrey900,
    surface = Color.White,
)

private val darkColorScheme = darkColorScheme(
    primary = Lime900,
    onPrimary = DarkGreen900,
    primaryContainer = DarkGreen700,
    onPrimaryContainer = Color.White,
    secondary = Lime700,
    onSecondary = DarkGreen700,
    background = DarkGreen900,
    onBackground = Lime900,
    onSurface = DarkGreen600,
    surface = DarkGreen900,
)