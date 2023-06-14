package com.mindpalais.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.mindpalais.android.R

private val DarkColorPalette = darkColors(
        primary = darkPrimary,
        primaryVariant = darkBlue,
        secondary = darkGreen
)

private val LightColorPalette = lightColors(
        primary = lightPrimary,
        primaryVariant = lightBlue,
        secondary = lightGreen

        /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun MindpalaisTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val interFontFamily = FontFamily(
        Font(R.font.inter_semibold, FontWeight.Light),
        Font(R.font.inter_semibold, FontWeight.Normal),
        Font(R.font.inter_semibold, FontWeight.Bold),
        Font(R.font.constance, FontWeight.SemiBold)
        // Add additional font styles as needed
    )

    MaterialTheme(
            colors = colors,
            typography = Typography(defaultFontFamily = interFontFamily),
            shapes = Shapes,
            content = content
    )
}