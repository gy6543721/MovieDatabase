package levilin.moviedatabase.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightGray = Color(0xFFFAFAFA)
val LightMediumGray = Color(0xFFD1CFCF)
val MediumGray = Color(0xFFACA9A9)
val MediumDarkGray = Color(0xFF6F6C6C)
val DarkGray = Color(0xFF3D3C3C)

val LightOrange = Color(0xFFF1AC47)
val Orange = Color(0xFFFF9800)
val DarkOrange = Color(0xFF533201)

val LightRed = Color(0xFFFF3939)
val LightGreen = Color(0xFF9CFF39)

val Colors.screenBackgroundColor: Color
    @Composable
    get() = if (isLight) LightGray else DarkGray

val Colors.screenTextColor: Color
    @Composable
    get() = if (isLight) DarkGray else LightGray

val Colors.buttonBackgroundColor: Color
    @Composable
    get() = if (isLight) LightGray else DarkGray

val Colors.buttonIconColor: Color
    @Composable
    get() = if (isLight) DarkGray else LightGray

val Colors.favouriteButtonColor: Color
    @Composable
    get() = if (isLight) LightMediumGray else MediumDarkGray

val Colors.indicatorRed: Color
    @Composable
    get() = if (isLight) Color.Red else LightRed

val Colors.indicatorGreen: Color
    @Composable
    get() = if (isLight) Color.Green else LightGreen
