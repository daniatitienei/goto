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
        colorScheme = if (isDarkTheme) darkColorScheme else lightColorScheme
    )
}

private val lightColorScheme = lightColorScheme(
    primary = Lime900,
    onPrimary = Color.White,
    primaryContainer = Lime900,
    onPrimaryContainer = Color.White,
    inversePrimary = DarkGreen900,
    secondary = Lime700,
    onSecondary = Color.White,
    secondaryContainer = Lime700,
    onSecondaryContainer = Color.White,
    onBackground = Lime900
)

private val darkColorScheme = darkColorScheme(
    primary = DarkGreen900,
    onPrimary = Color.White,
    primaryContainer = DarkGreen900,
    onPrimaryContainer = Color.White,
    inversePrimary = DarkGreen900,
    secondary = DarkGreen700,
    onSecondary = Color.White,
    secondaryContainer = DarkGreen700,
    onSecondaryContainer = Color.White,
    background = DarkGreen900,
    onBackground = Lime900,
)