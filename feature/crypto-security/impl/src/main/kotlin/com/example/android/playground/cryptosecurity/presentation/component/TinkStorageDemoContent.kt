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
internal fun TinkStorageDemoContent(
    state: StorageDemoState,
    onIntent: (StorageDemoIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = "Scenario 3 — Tink + DataStore",
                onNavigationClick = { onIntent(StorageDemoIntent.NavigateBack) },
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
                        "AndroidKeysetManager creates an AES256-GCM Tink keyset.",
                        "The keyset JSON is stored in SharedPrefs, wrapped by a Keystore master key.",
                        "aead.encrypt(plaintext, associatedData) → ciphertext (Tink embeds the IV).",
                        "Ciphertext stored as a single ByteArray entry in DataStore.",
                        "On load: aead.decrypt(ciphertext, associatedData) recovers the plaintext.",
                    ),
            )

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text =
                        "COMPARE WITH SCENARIO 2\n" +
                            "Same result — but Tink handles key generation, IV embedding, and keyset " +
                            "versioning automatically. No manual ByteArray wiring required.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(12.dp),
                )
            }

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

            // Tink bundles the IV inside the ciphertext — no separate IV to show
            state.savedCiphertextHex?.let { ct ->
                Text(
                    "Stored in DataStore (encrypted):",
                    style = MaterialTheme.typography.labelLarge,
                )
                CryptoInfoCard(
                    label = "Tink ciphertext (hex) — IV embedded by Tink",
                    value = ct,
                )
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
private fun TinkStorageDemoContentPreview() {
    PreviewContainer {
        TinkStorageDemoContent(
            state = StorageDemoState(),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}

@FullPreview
@Composable
private fun TinkStorageDemoContentLoadedPreview() {
    PreviewContainer {
        TinkStorageDemoContent(
            state =
                StorageDemoState(
                    savedCiphertextHex = "deadbeefcafe01234567",
                    loadedValue = "Hello, Tink world!",
                ),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}
