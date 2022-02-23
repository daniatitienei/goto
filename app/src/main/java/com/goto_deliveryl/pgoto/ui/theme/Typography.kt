package com.goto_deliveryl.pgoto.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.goto_deliveryl.pgoto.R

val sanFranciscoFamily = FontFamily(
    Font(R.font.sf_light, weight = FontWeight.Light),
    Font(R.font.sf_regular),
    Font(R.font.sf_medium, weight = FontWeight.Medium),
    Font(R.font.sf_semibold, weight = FontWeight.SemiBold),
    Font(R.font.sf_bold, weight = FontWeight.Bold),
    Font(R.font.sf_heavy, weight = FontWeight.ExtraBold),
)

val GotoTypography = androidx.compose.material3.Typography(
    bodyLarge = TextStyle(
        fontFamily = sanFranciscoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = sanFranciscoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = sanFranciscoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),
    titleLarge = TextStyle(
        fontFamily = sanFranciscoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = sanFranciscoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 124.sp
    ),
    titleSmall = TextStyle(
        fontFamily = sanFranciscoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    displayLarge = TextStyle(
        fontFamily = sanFranciscoFamily,
        fontWeight = FontWeight.Normal
    ),
    displayMedium = TextStyle(
        fontFamily = sanFranciscoFamily,
        fontWeight = FontWeight.Normal
    ),
    displaySmall = TextStyle(
        fontFamily = sanFranciscoFamily,
        fontWeight = FontWeight.Normal
    ),
    labelLarge = TextStyle(
        fontFamily = sanFranciscoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = sanFranciscoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = sanFranciscoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = sanFranciscoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = sanFranciscoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = sanFranciscoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
)