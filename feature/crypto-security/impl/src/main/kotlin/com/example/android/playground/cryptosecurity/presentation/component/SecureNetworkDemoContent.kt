package com.example.android.playground.cryptosecurity.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.FullPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.cryptosecurity.presentation.intent.SecureNetworkDemoIntent
import com.example.android.playground.cryptosecurity.presentation.state.SecureNetworkDemoState

@Composable
internal fun SecureNetworkDemoContent(
    state: SecureNetworkDemoState,
    onIntent: (SecureNetworkDemoIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = "Scenario 1 — Secure Network Fetch",
                onNavigationClick = { onIntent(SecureNetworkDemoIntent.NavigateBack) },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            HowItWorksCard(
                steps = listOf(
                    "Server encrypts the response body with AES-256-GCM.",
                    "The Base64-encoded IV and ciphertext arrive as a JSON payload.",
                    "The app decrypts using an AES key stored in Android Keystore.",
                    "The key never leaves the secure hardware enclave.",
                ),
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onIntent(SecureNetworkDemoIntent.FetchData) },
                    enabled = !state.isLoading,
                    modifier = Modifier.weight(1f),
                ) {
                    if (state.isLoading) {
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                strokeWidth = 2.dp,
                            )
                        }
                    } else {
                        Text("Fetch Encrypted Payload")
                    }
                }
                OutlinedButton(
                    onClick = { onIntent(SecureNetworkDemoIntent.Clear) },
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Clear")
                }
            }

            state.serverPayload?.let { payload ->
                Text(
                    "Server Response (encrypted):",
                    style = MaterialTheme.typography.labelLarge,
                )
                CryptoInfoCard(label = "IV (Base64)", value = payload.iv)
                CryptoInfoCard(label = "Ciphertext (Base64)", value = payload.encryptedPayload)

                Button(
                    onClick = { onIntent(SecureNetworkDemoIntent.DecryptPayload) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.decryptedMessage == null,
                ) {
                    Text("Decrypt with Keystore Key")
                }
            }

            state.decryptedMessage?.let { plaintext ->
                Text(
                    "Decrypted message:",
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
private fun SecureNetworkDemoContentPreview() {
    PreviewContainer {
        SecureNetworkDemoContent(
            state = SecureNetworkDemoState(),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}

@FullPreview
@Composable
private fun SecureNetworkDemoContentLoadingPreview() {
    PreviewContainer {
        SecureNetworkDemoContent(
            state = SecureNetworkDemoState(isLoading = true),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}
