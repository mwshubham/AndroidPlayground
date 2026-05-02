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
import com.example.android.playground.interappcomm.presentation.intent.BroadcastIntent
import com.example.android.playground.interappcomm.presentation.state.BroadcastState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BroadcastContent(
    state: BroadcastState,
    onIntent: (BroadcastIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Broadcast",
                onNavigationClick = { onIntent(BroadcastIntent.NavigateBack) },
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
                title = "Security: Signature Permission on Broadcast",
                mechanism = "sendBroadcast(intent, INTER_APP_COMM) + receiver android:permission",
                notes =
                    listOf(
                        "The second param in sendBroadcast enforces the RECEIVER must hold the permission.",
                        "The receiver's android:permission ensures only a permissioned SENDER can deliver.",
                        "Both combined prevent eavesdropping AND spoofing.",
                    ),
            )
            OutlinedTextField(
                value = state.inputText,
                onValueChange = { onIntent(BroadcastIntent.OnInputChanged(it)) },
                label = { Text("Message") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onIntent(BroadcastIntent.SendBroadcast) },
                    modifier = Modifier.weight(1f),
                ) { Text("Send") }
                OutlinedButton(
                    onClick = { onIntent(BroadcastIntent.ClearMessages) },
                    modifier = Modifier.weight(1f),
                ) { Text("Clear") }
            }
            Text(
                text = "Message log",
                style = MaterialTheme.typography.titleSmall,
            )
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
private fun BroadcastContentPreview() {
    PreviewContainer {
        BroadcastContent(
            state =
                BroadcastState(
                    currentPackage = "com.example.android.playground",
                    targetPackage = "com.example.android.playground.variant",
                ),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}
