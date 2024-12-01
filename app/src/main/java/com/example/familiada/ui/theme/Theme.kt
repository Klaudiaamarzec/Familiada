package com.example.familiada.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFC107), // Żółty
    secondary = Color(0xFF757575), // Szary
    tertiary = Color(0xFFD32F2F), // Czerwony
    background = Color.Black,
    surface = Color(0xFF121212), // Ciemny szary
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    primaryContainer = Color(0xFFFFC107) // Kolor nagłówków

)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFFC107), // Żółty
    secondary = Color(0xFFBDBDBD), // Jasny szary
    tertiary = Color(0xFFD32F2F), // Czerwony
    background = Color(0xFFFFFBFE), // Biały,ale nie do końca
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    primaryContainer = Color.Black // Kolor nagłówków
)

@Composable
fun FamiliadaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}

val ColorScheme.headlineColor: Color
    get() = this.primaryContainer