package com.example.android.playground.interappcomm.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.interappcomm.presentation.intent.ContentProviderIntent
import com.example.android.playground.interappcomm.presentation.state.ContentProviderState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ContentProviderContent(
    state: ContentProviderState,
    onIntent: (ContentProviderIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "ContentProvider",
                onNavigationClick = { onIntent(ContentProviderIntent.NavigateBack) },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SecurityInfoCard(
                title = "Security: readPermission + writePermission",
                mechanism = "Separate read/write signature-level permissions on the provider",
                notes = listOf(
                    "Only apps with the same signing cert can read or write data.",
                    "The OS enforces this inside Binder — no app-level check needed.",
                    "Binder.getCallingUid() is logged on every access for auditing.",
                ),
            )
            OutlinedTextField(
                value = state.inputText,
                onValueChange = { onIntent(ContentProviderIntent.OnInputChanged(it)) },
                label = { Text("Message to write") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onIntent(ContentProviderIntent.WriteToOtherApp) },
                    modifier = Modifier.weight(1f),
                ) { Text("Write") }
                OutlinedButton(
                    onClick = { onIntent(ContentProviderIntent.ReadFromOtherApp) },
                    modifier = Modifier.weight(1f),
                ) { Text("Read remote") }
            }
            if (state.remoteMessages.isNotEmpty()) {
                Text(text = "Remote messages (from other app):")
                state.remoteMessages.forEach { msg ->
                    Text(
                        text = "• ${msg.content}",
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                    )
                }
            }
            if (state.ownMessages.isNotEmpty()) {
                Text(text = "Own messages (visible to other app):")
                state.ownMessages.forEach { msg ->
                    Text(
                        text = "• ${msg.content}",
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun ContentProviderContentPreview() {
    PreviewContainer {
        ContentProviderContent(
            state = ContentProviderState(
                currentPackage = "com.example.android.playground",
                targetPackage = "com.example.android.playground.variant",
            ),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}
