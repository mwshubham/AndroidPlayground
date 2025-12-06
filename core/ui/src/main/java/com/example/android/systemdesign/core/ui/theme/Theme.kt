package com.example.android.systemdesign.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Primary80,
    onPrimary = Color(0xFF003300),
    primaryContainer = PrimaryContainer80,
    onPrimaryContainer = Color(0xFFE8F5E8),

    secondary = Secondary80,
    onSecondary = Color(0xFF001122),
    secondaryContainer = SecondaryContainer80,
    onSecondaryContainer = Color(0xFFE3F2FD),

    tertiary = Tertiary80,
    onTertiary = Color(0xFF220033),
    tertiaryContainer = TertiaryContainer80,
    onTertiaryContainer = Color(0xFFF3E5F5),

    background = SurfaceDark,
    onBackground = OnSurfaceDark,

    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,

    error = Color(0xFFFF6B6B),
    onError = Color(0xFF330000),
    errorContainer = Color(0xFF8B0000),
    onErrorContainer = Color(0xFFFFE6E6),

    outline = Color(0xFF404040),
    outlineVariant = Color(0xFF303030)
)

private val LightColorScheme = lightColorScheme(
    primary = Primary40,
    onPrimary = Color.White,
    primaryContainer = PrimaryContainer40,
    onPrimaryContainer = Color.White,

    secondary = Secondary40,
    onSecondary = Color.White,
    secondaryContainer = SecondaryContainer40,
    onSecondaryContainer = Color.White,

    tertiary = Tertiary40,
    onTertiary = Color.White,
    tertiaryContainer = TertiaryContainer40,
    onTertiaryContainer = Color.White,

    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),

    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF424242),

    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFFF5722),
    onErrorContainer = Color.White,

    outline = Color(0xFFBDBDBD),
    outlineVariant = Color(0xFFE0E0E0)
)

@Composable
fun AndroidSystemDesignTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
