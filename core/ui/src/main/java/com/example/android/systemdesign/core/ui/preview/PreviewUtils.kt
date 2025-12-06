package com.example.android.systemdesign.core.ui.preview

import androidx.compose.runtime.Composable
import com.example.android.systemdesign.core.ui.preview.PreviewUtils.ThemedPreview
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
            darkTheme = darkTheme
        ) {
            content()
        }
    }
}

/**
 * Extension functions for easier preview creation
 */
@Composable
fun PreviewContainer(
    darkTheme: Boolean = androidx.compose.foundation.isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    ThemedPreview(
        darkTheme = darkTheme,
        content = content
    )
}
