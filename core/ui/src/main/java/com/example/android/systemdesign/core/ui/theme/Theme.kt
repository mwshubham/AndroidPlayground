package com.example.android.systemdesign.core.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Primary80,
    onPrimary = DarkOnPrimary,
    primaryContainer = PrimaryContainer80,
    onPrimaryContainer = DarkOnPrimaryContainer,

    secondary = Secondary80,
    onSecondary = DarkOnSecondary,
    secondaryContainer = SecondaryContainer80,
    onSecondaryContainer = DarkOnSecondaryContainer,

    tertiary = Tertiary80,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = TertiaryContainer80,
    onTertiaryContainer = DarkOnTertiaryContainer,

    background = SurfaceDark,
    onBackground = OnSurfaceDark,

    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,

    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainerLowest = SurfaceContainerLowestDark,
    surfaceContainerHighest = SurfaceContainerHighestDark,

    inverseSurface = InverseSurfaceDark,
    inverseOnSurface = InverseOnSurfaceDark,
    inversePrimary = InversePrimaryDark,

    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,

    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant
)

private val LightColorScheme = lightColorScheme(
    primary = Primary40,
    onPrimary = LightOnPrimary,
    primaryContainer = PrimaryContainer40,
    onPrimaryContainer = LightOnPrimaryContainer,

    secondary = Secondary40,
    onSecondary = LightOnSecondary,
    secondaryContainer = SecondaryContainer40,
    onSecondaryContainer = LightOnSecondaryContainer,

    tertiary = Tertiary40,
    onTertiary = LightOnTertiary,
    tertiaryContainer = TertiaryContainer40,
    onTertiaryContainer = LightOnTertiaryContainer,

    background = LightBackground,
    onBackground = LightOnBackground,

    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,

    surfaceContainer = SurfaceContainerLight,
    surfaceContainerHigh = SurfaceContainerHighLight,
    surfaceContainerLow = SurfaceContainerLowLight,
    surfaceContainerLowest = SurfaceContainerLowestLight,
    surfaceContainerHighest = SurfaceContainerHighestLight,

    inverseSurface = InverseSurfaceLight,
    inverseOnSurface = InverseOnSurfaceLight,
    inversePrimary = InversePrimaryLight,

    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,

    outline = LightOutline,
    outlineVariant = LightOutlineVariant
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
