package com.example.android.playground.cryptosecurity.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.FullPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.cryptosecurity.presentation.intent.SendEncryptedDemoIntent
import com.example.android.playground.cryptosecurity.presentation.state.SendEncryptedDemoState

@Composable
internal fun SendEncryptedDemoContent(
    state: SendEncryptedDemoState,
    onIntent: (SendEncryptedDemoIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = "Scenario 4 — Send Encrypted to Server",
                onNavigationClick = { onIntent(SendEncryptedDemoIntent.NavigateBack) },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            HowItWorksCard(
                steps =
                    listOf(
                        "A fresh one-time AES-256 key is generated for each message.",
                        "The message is encrypted with the AES key (AES-GCM).",
                        "The AES key is encrypted with the server's RSA-2048 public key (OAEP).",
                        "Both are sent to the server. Only the server's RSA private key can recover the AES key.",
                        "DEMO: The private key is in-process here to simulate server-side decryption.",
                    ),
            )

            OutlinedTextField(
                value = state.inputMessage,
                onValueChange = { onIntent(SendEncryptedDemoIntent.MessageChanged(it)) },
                label = { Text("Message to send") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onIntent(SendEncryptedDemoIntent.EncryptAndSend) },
                    enabled = !state.isEncrypting && state.inputMessage.isNotBlank(),
                    modifier = Modifier.weight(1f),
                ) {
                    if (state.isEncrypting) {
                        CircularProgressIndicator(strokeWidth = 2.dp)
                    } else {
                        Text("Encrypt & Send")
                    }
                }
                OutlinedButton(
                    onClick = { onIntent(SendEncryptedDemoIntent.Clear) },
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Clear")
                }
            }

            state.encryptedPayload?.let { payload ->
                Text(
                    "Payload sent to server:",
                    style = MaterialTheme.typography.labelLarge,
                )
                CryptoInfoCard(
                    label = "RSA-OAEP wrapped AES key (${payload.encryptedKey.size} bytes hex)",
                    value = payload.encryptedKey.toHex(),
                )
                CryptoInfoCard(
                    label = "AES-GCM IV (${payload.iv.size} bytes hex)",
                    value = payload.iv.toHex(),
                )
                CryptoInfoCard(
                    label = "AES-GCM ciphertext (${payload.encryptedPayload.size} bytes hex)",
                    value = payload.encryptedPayload.toHex(),
                )

                Button(
                    onClick = { onIntent(SendEncryptedDemoIntent.SimulateServerDecrypt) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.serverDecryptedMessage == null,
                ) {
                    Text("Simulate Server Decrypt")
                }
            }

            state.serverDecryptedMessage?.let { plaintext ->
                Text(
                    "Server decrypted message:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = plaintext,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(12.dp),
                    )
                }
            }

            state.error?.let { error ->
                Text(
                    text = "Error: $error",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

// ---- Previews ----

@FullPreview
@Composable
private fun SendEncryptedDemoContentPreview() {
    PreviewContainer {
        SendEncryptedDemoContent(
            state = SendEncryptedDemoState(),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}

@FullPreview
@Composable
private fun SendEncryptedDemoContentEncryptingPreview() {
    PreviewContainer {
        SendEncryptedDemoContent(
            state = SendEncryptedDemoState(isEncrypting = true),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}

private fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }
