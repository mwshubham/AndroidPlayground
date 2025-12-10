package com.example.android.systemdesign.core.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.android.systemdesign.core.ui.preview.ComponentPreview
import com.example.android.systemdesign.core.ui.preview.PreviewContainer

/**
 * A reusable top app bar component that provides consistent styling across the application.
 *
 * @param modifier Modifier to be applied to the top app bar
 * @param title The title to display in the app bar
 * @param onNavigationClick Callback for when the navigation icon is clicked
 * @param actions Action items to display on the right side of the app bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onNavigationClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@ComponentPreview
@Composable
fun AppTopAppBarBasicPreview() {
    PreviewContainer {
        AppTopAppBar(
            title = "Android System Design",
            onNavigationClick = {}
        )
    }
}

@ComponentPreview
@Composable
fun AppTopAppBarWithActionsPreview() {
    PreviewContainer {
        AppTopAppBar(
            title = "Android System Design Android System Design Android System Design",
            onNavigationClick = {},
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save"
                    )
                }
            }
        )
    }
}
