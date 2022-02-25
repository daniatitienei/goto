package com.goto_delivery.pgoto.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.goto_delivery.pgoto.R

val NevisBoldFamily = FontFamily(
    Font(R.font.nevis_bold, weight = FontWeight.Normal),
)

val LouisGeorgeCafeFamily = FontFamily(
    Font(R.font.louis_george_cafe, weight = FontWeight.Normal),
    Font(R.font.louis_george_cafe_bold, weight = FontWeight.Bold),
    Font(R.font.louis_george_cafe_light, weight = FontWeight.Light),
)

val GotoTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = LouisGeorgeCafeFamily, fontSize = 16.sp, fontWeight = FontWeight.Bold
    ),
    bodyMedium = TextStyle(
        fontFamily = LouisGeorgeCafeFamily, fontSize = 14.sp, fontWeight = FontWeight.Bold
    ),
    bodySmall = TextStyle(
        fontFamily = LouisGeorgeCafeFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold
    ),
    titleLarge = TextStyle(
        fontFamily = NevisBoldFamily, fontSize = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = NevisBoldFamily, fontSize = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = NevisBoldFamily, fontSize = 20.sp
    ),
    displayLarge = TextStyle(
        fontFamily = NevisBoldFamily, fontSize = 28.sp
    ),
    displayMedium = TextStyle(
        fontFamily = NevisBoldFamily, fontSize = 24.sp
    ),
    displaySmall = TextStyle(
        fontFamily = NevisBoldFamily, fontSize = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = NevisBoldFamily, fontSize = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = NevisBoldFamily, fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = NevisBoldFamily, fontSize = 11.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = NevisBoldFamily, fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = NevisBoldFamily, fontSize = 28.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = NevisBoldFamily, fontSize = 24.sp
    ),
)