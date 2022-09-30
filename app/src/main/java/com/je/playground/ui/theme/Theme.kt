package com.je.playground.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    //Main colors
    primary = darkPrimary,
    primaryVariant = darkPrimaryVariant,

    //Text
    secondary = darkSecondary,
    secondaryVariant = darkSecondaryVariant,

    //Green/Red
    onPrimary = onPrimary,
    onSecondary = onSecondary,

    background = darkPrimary,
    onBackground = darkSecondary
)


private val LightColorPalette = lightColors(
    //Main colors
    primary = lightPrimary,
    primaryVariant = lightPrimaryVariant,

    //Text
    secondary = lightSecondary,
    secondaryVariant = lightSecondaryVariant,

    //Green/Red
    onPrimary = onPrimary,
    onSecondary = onSecondary,

    background = lightPrimary,
    onBackground = lightSecondary

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
fun PlaygroundTheme(
    darkTheme : Boolean = isSystemInDarkTheme(),
    content : @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        if (darkTheme) {
            DarkColorPalette.background
        } else {
            LightColorPalette.background
        }
    )

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}