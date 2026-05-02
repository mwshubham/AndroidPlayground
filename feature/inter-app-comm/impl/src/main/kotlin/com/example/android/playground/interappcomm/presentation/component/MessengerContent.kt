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
import com.example.android.playground.interappcomm.presentation.intent.MessengerIntent
import com.example.android.playground.interappcomm.presentation.state.MessengerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MessengerContent(
    state: MessengerState,
    onIntent: (MessengerIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Messenger",
                onNavigationClick = { onIntent(MessengerIntent.NavigateBack) },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SecurityInfoCard(
                title = "Security: android:permission on Service",
                mechanism = "android:permission=\"INTER_APP_COMM\" on the service declaration",
                notes =
                    listOf(
                        "Only apps holding the signature permission can bind to this service.",
                        "The OS rejects bindService() calls from apps without the permission.",
                        "No manual check inside onBind() is needed — the OS handles it.",
                    ),
            )
            ConnectionStatusBanner(
                isConnected = state.isConnected,
                isConnecting = state.isConnecting,
                targetPackage = state.targetPackage,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onIntent(MessengerIntent.Connect) },
                    enabled = !state.isConnected && !state.isConnecting,
                    modifier = Modifier.weight(1f),
                ) { Text("Bind") }
                OutlinedButton(
                    onClick = { onIntent(MessengerIntent.Disconnect) },
                    enabled = state.isConnected,
                    modifier = Modifier.weight(1f),
                ) { Text("Unbind") }
            }
            OutlinedTextField(
                value = state.inputText,
                onValueChange = { onIntent(MessengerIntent.OnInputChanged(it)) },
                label = { Text("Message") },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isConnected,
                singleLine = true,
            )
            Button(
                onClick = { onIntent(MessengerIntent.SendMessage) },
                enabled = state.isConnected && state.inputText.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Send") }
            Text(text = "Message log:")
            MessageLogList(
                messages = state.messages,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun MessengerContentPreview() {
    PreviewContainer {
        MessengerContent(
            state =
                MessengerState(
                    currentPackage = "com.example.android.playground",
                    targetPackage = "com.example.android.playground.variant",
                    isConnected = false,
                ),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}
