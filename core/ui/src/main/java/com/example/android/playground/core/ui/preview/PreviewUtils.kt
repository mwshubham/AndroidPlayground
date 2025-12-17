package com.example.android.playground.core.ui.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.android.playground.core.ui.preview.PreviewUtils.ThemedPreview
import com.example.android.playground.core.ui.theme.AppTheme

/**
 * Utility composables for consistent preview theming
 */
object PreviewUtils {
    @Composable
    fun ThemedPreview(
        modifier: Modifier = Modifier,
        darkTheme: Boolean = false,
        content: @Composable () -> Unit,
    ) {
        AppTheme(
            darkTheme = darkTheme,
        ) {
            Box(modifier = modifier) {
                content()
            }
        }
    }
}

/**
 * Extension functions for easier preview creation
 */
@Composable
fun PreviewContainer(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = androidx.compose.foundation.isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    ThemedPreview(
        modifier = modifier,
        darkTheme = darkTheme,
        content = content,
    )
}
