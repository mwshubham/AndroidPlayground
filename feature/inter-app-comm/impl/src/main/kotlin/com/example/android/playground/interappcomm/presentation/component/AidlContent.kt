package com.example.android.playground.interappcomm.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.example.android.playground.interappcomm.presentation.intent.AidlIntent
import com.example.android.playground.interappcomm.presentation.state.AidlState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AidlContent(
    state: AidlState,
    onIntent: (AidlIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "AIDL",
                onNavigationClick = { onIntent(AidlIntent.NavigateBack) },
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
                title = "Security: enforceCallingPermission() in every stub method",
                mechanism = "IInterAppService.Stub overrides enforce the signature permission explicitly",
                notes = listOf(
                    "android:permission on the service blocks bind() from unknown callers.",
                    "enforceCallingPermission() inside each method is defence-in-depth for oneway calls.",
                    "AIDL calls run on Binder thread pool — synchronous RPC, not callbacks.",
                    "Use Dispatchers.IO to avoid blocking the main thread.",
                ),
            )
            ConnectionStatusBanner(
                isConnected = state.isConnected,
                isConnecting = state.isConnecting,
                targetPackage = state.targetPackage,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onIntent(AidlIntent.Connect) },
                    enabled = !state.isConnected && !state.isConnecting,
                    modifier = Modifier.weight(1f),
                ) { Text("Bind") }
                OutlinedButton(
                    onClick = { onIntent(AidlIntent.Disconnect) },
                    enabled = state.isConnected,
                    modifier = Modifier.weight(1f),
                ) { Text("Unbind") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onIntent(AidlIntent.Ping) },
                    enabled = state.isConnected,
                    modifier = Modifier.weight(1f),
                ) { Text("ping()") }
                OutlinedButton(
                    onClick = { onIntent(AidlIntent.GetMessages) },
                    enabled = state.isConnected,
                    modifier = Modifier.weight(1f),
                ) { Text("getMessages()") }
            }
            state.pingResult?.let { result ->
                Text(
                    text = "ping result: $result",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            if (state.remoteMessages.isNotEmpty()) {
                Text(
                    text = "Remote messages (${state.remoteMessages.size}):",
                    style = MaterialTheme.typography.titleSmall,
                )
                state.remoteMessages.forEach { msg ->
                    Text(
                        text = "• $msg",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            OutlinedTextField(
                value = state.inputText,
                onValueChange = { onIntent(AidlIntent.OnInputChanged(it)) },
                label = { Text("Message (postMessage — oneway)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isConnected,
                singleLine = true,
            )
            Button(
                onClick = { onIntent(AidlIntent.PostMessage) },
                enabled = state.isConnected && state.inputText.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
            ) { Text("postMessage() — oneway") }
            if (state.messages.isNotEmpty()) {
                Text(text = "Sent messages:")
                MessageLogList(
                    messages = state.messages,
                    modifier = Modifier.weight(1f),
                )
            }
            state.error?.let { err ->
                Text(
                    text = err,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun AidlContentPreview() {
    PreviewContainer {
        AidlContent(
            state = AidlState(
                currentPackage = "com.example.android.playground",
                targetPackage = "com.example.android.playground.variant",
                isConnected = true,
                pingResult = "pong from com.example.android.playground.variant",
            ),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}
