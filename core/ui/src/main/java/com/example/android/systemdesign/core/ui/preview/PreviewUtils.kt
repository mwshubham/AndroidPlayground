package com.example.android.systemdesign.core.ui.preview

import androidx.compose.runtime.Composable
import com.example.android.systemdesign.core.ui.theme.AppTheme

/**
 * Utility composables for consistent preview theming
 */
object PreviewUtils {

    @Composable
    fun ThemedPreview(
        darkTheme: Boolean = false,
        content: @Composable () -> Unit
    ) {
        AppTheme(
            darkTheme = darkTheme,
            dynamicColor = false // Disable dynamic color for consistent previews
        ) {
            content()
        }
    }

    @Composable
    fun LightThemePreview(
        content: @Composable () -> Unit
    ) {
        ThemedPreview(darkTheme = false, content = content)
    }

    @Composable
    fun DarkThemePreview(
        content: @Composable () -> Unit
    ) {
        ThemedPreview(darkTheme = true, content = content)
    }
}

/**
 * Extension functions for easier preview creation
 */
@Composable
fun PreviewContainer(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    PreviewUtils.ThemedPreview(darkTheme = darkTheme, content = content)
}
