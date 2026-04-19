package com.example.android.playground.cryptosecurity.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.FullPreview
import com.example.android.playground.core.ui.preview.PreviewContainer

@Composable
internal fun CryptoSecurityHomeContent(
    onNavigateBack: () -> Unit,
    onSecureNetworkClick: () -> Unit,
    onKeystoreStorageClick: () -> Unit,
    onTinkStorageClick: () -> Unit,
    onSendEncryptedClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = "Android Security & Cryptography",
                onNavigationClick = onNavigateBack,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Explore real-world Android security patterns via hands-on demos.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp),
            )

            ScenarioCard(
                icon = Icons.Default.Cloud,
                title = "Scenario 1 — Secure Network Fetch",
                description = "Receive an AES-256-GCM encrypted payload from the server (Base64 IV + ciphertext) and decrypt it using an Android Keystore-backed key.",
                onTryDemo = onSecureNetworkClick,
            )

            ScenarioCard(
                icon = Icons.Default.Lock,
                title = "Scenario 2 — Encrypted Storage (Option B)",
                description = "Manual approach: Android Keystore + AES-256-GCM + DataStore. " +
                    "You control every step — key generation, IV handling, and ciphertext storage.",
                onTryDemo = onKeystoreStorageClick,
            )

            ScenarioCard(
                icon = Icons.Default.Security,
                title = "Scenario 3 — Encrypted Storage (Option A)",
                description = "Tink + DataStore approach. AndroidKeysetManager handles the " +
                    "keyset lifecycle and Keystore wrapping. Same security, far fewer lines of code.",
                onTryDemo = onTinkStorageClick,
            )

            ScenarioCard(
                icon = Icons.Default.Send,
                title = "Scenario 4 — Send Encrypted to Server",
                description = "Hybrid RSA-OAEP + AES-256-GCM encryption. A one-time AES key " +
                    "encrypts the payload; the AES key is wrapped with the server's RSA public key. " +
                    "Only the server can read the message.",
                onTryDemo = onSendEncryptedClick,
            )
        }
    }
}

// ---- Previews ----

@FullPreview
@Composable
private fun CryptoSecurityHomeContentPreview() {
    PreviewContainer {
        CryptoSecurityHomeContent(
            onNavigateBack = {},
            onSecureNetworkClick = {},
            onKeystoreStorageClick = {},
            onTinkStorageClick = {},
            onSendEncryptedClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
