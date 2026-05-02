package com.example.android.playground.interappcomm.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer

/**
 * Shows the connection state for Messenger and AIDL bound services.
 */
@Composable
fun ConnectionStatusBanner(
    isConnected: Boolean,
    isConnecting: Boolean,
    targetPackage: String,
    modifier: Modifier = Modifier,
) {
    val (icon, tint, label) =
        when {
            isConnecting ->
                Triple(
                    Icons.Default.Sync,
                    MaterialTheme.colorScheme.primary,
                    "Connecting to $targetPackage…",
                )
            isConnected ->
                Triple(
                    Icons.Default.CheckCircle,
                    MaterialTheme.colorScheme.primary,
                    "Connected to $targetPackage",
                )
            else ->
                Triple(
                    Icons.Default.Error,
                    MaterialTheme.colorScheme.error,
                    "Not connected — tap Bind",
                )
        }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (isConnected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    },
            ),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color =
                    if (isConnected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun ConnectionStatusBannerConnectedPreview() {
    PreviewContainer {
        ConnectionStatusBanner(
            isConnected = true,
            isConnecting = false,
            targetPackage = "com.example.playground.variant",
        )
    }
}

@ComponentPreview
@Composable
private fun ConnectionStatusBannerDisconnectedPreview() {
    PreviewContainer {
        ConnectionStatusBanner(
            isConnected = false,
            isConnecting = false,
            targetPackage = "com.example.playground.variant",
        )
    }
}
