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
import com.example.android.playground.cryptosecurity.presentation.intent.StorageDemoIntent
import com.example.android.playground.cryptosecurity.presentation.state.StorageDemoState

@Composable
internal fun KeystoreStorageDemoContent(
    state: StorageDemoState,
    onIntent: (StorageDemoIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = "Scenario 2 — Keystore + DataStore",
                onNavigationClick = { onIntent(StorageDemoIntent.NavigateBack) },
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
                    "AES-256 key is generated inside Android Keystore (hardware-backed).",
                    "encrypt(plaintext) → EncryptedData(iv: ByteArray, ciphertext: ByteArray).",
                    "Both IV and ciphertext are stored as raw ByteArray entries in DataStore.",
                    "On load: read IV + ciphertext from DataStore → decrypt with Keystore key.",
                    "The key never leaves the secure enclave.",
                ),
            )

            OutlinedTextField(
                value = state.inputKey,
                onValueChange = { onIntent(StorageDemoIntent.KeyChanged(it)) },
                label = { Text("Storage Key") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = state.inputValue,
                onValueChange = { onIntent(StorageDemoIntent.ValueChanged(it)) },
                label = { Text("Value to Encrypt & Save") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onIntent(StorageDemoIntent.EncryptSave) },
                    enabled = !state.isSaving && state.inputValue.isNotBlank(),
                    modifier = Modifier.weight(1f),
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(strokeWidth = 2.dp)
                    } else {
                        Text("Encrypt & Save")
                    }
                }
                Button(
                    onClick = { onIntent(StorageDemoIntent.LoadDecrypt) },
                    enabled = !state.isLoading,
                    modifier = Modifier.weight(1f),
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(strokeWidth = 2.dp)
                    } else {
                        Text("Load & Decrypt")
                    }
                }
                OutlinedButton(onClick = { onIntent(StorageDemoIntent.Clear) }) {
                    Text("Clear")
                }
            }

            state.savedIvHex?.let { iv ->
                Text("Stored in DataStore (encrypted):", style = MaterialTheme.typography.labelLarge)
                CryptoInfoCard(label = "IV (hex)", value = iv)
            }
            state.savedCiphertextHex?.let { ct ->
                CryptoInfoCard(label = "Ciphertext (hex)", value = ct)
            }

            state.loadedValue?.let { value ->
                Text(
                    "Decrypted value:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = value,
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
private fun KeystoreStorageDemoContentPreview() {
    PreviewContainer {
        KeystoreStorageDemoContent(
            state = StorageDemoState(),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}

@FullPreview
@Composable
private fun KeystoreStorageDemoContentLoadedPreview() {
    PreviewContainer {
        KeystoreStorageDemoContent(
            state = StorageDemoState(
                inputKey = "my_secret_key",
                savedIvHex = "a1b2c3d4e5f6",
                savedCiphertextHex = "deadbeefcafe0123",
                loadedValue = "Hello, decrypted world!",
            ),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}
