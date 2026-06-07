package com.example.movielist.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Cinema palette: IMDb-style gold on near-black with warm dark surfaces.
val Gold = Color(0xFFF5C518)
val GoldDim = Color(0xFFC9A200)
val NearBlack = Color(0xFF0B0B0F)
val Surface1 = Color(0xFF16161D)
val Surface2 = Color(0xFF23232E)
val TextHigh = Color(0xFFEDEDF0)
val TextDim = Color(0xFF9A9AA6)
val CineRed = Color(0xFFFF5A5F)

private val CinemaColors = darkColorScheme(
    primary = Gold,
    onPrimary = Color(0xFF1B1600),
    secondary = GoldDim,
    onSecondary = Color(0xFF1B1600),
    background = NearBlack,
    onBackground = TextHigh,
    surface = Surface1,
    onSurface = TextHigh,
    surfaceVariant = Surface2,
    onSurfaceVariant = TextDim,
    error = CineRed,
    onError = Color(0xFF1B1600),
    outline = Color(0xFF34343F),
)

private val CinemaTypography = Typography(
    titleLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp, letterSpacing = 0.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 17.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp),
    bodySmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp, letterSpacing = 0.2.sp),
)

@Composable
fun CineTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CinemaColors,
        typography = CinemaTypography,
        content = content
    )
}
