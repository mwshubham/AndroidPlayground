package com.example.android.playground.cryptosecurity.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer

@Composable
internal fun ScenarioCard(
    icon: ImageVector,
    title: String,
    description: String,
    onTryDemo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            TextButton(onClick = onTryDemo) {
                Text("Try Demo")
            }
        }
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun ScenarioCardPreview() {
    PreviewContainer {
        ScenarioCard(
            icon = Icons.Default.Lock,
            title = "Scenario 1 — Secure Network Fetch",
            description = "Receive an AES-256-GCM encrypted payload from the server and decrypt it using an Android Keystore-backed key.",
            onTryDemo = {},
        )
    }
}
