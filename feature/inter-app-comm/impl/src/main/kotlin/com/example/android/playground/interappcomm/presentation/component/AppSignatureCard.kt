package com.example.android.playground.interappcomm.presentation.component

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer

/**
 * Displays the SHA-256 signing-certificate fingerprint for both the current app
 * and the other flavor's app. A visual match indicator shows whether the signature
 * permission will be auto-granted (matching certs) or silently denied (different certs).
 *
 * The fingerprints can be manually cross-checked with:
 *   `apksigner verify --print-certs app.apk`
 *
 * @param currentPackage       Package name of the running app.
 * @param currentFingerprint   SHA-256 fingerprint of this app's signing cert ("AA:BB:CC…" or null).
 * @param targetPackage        Package name of the other flavor.
 * @param targetFingerprint    SHA-256 fingerprint of the other app's cert, or null if not installed.
 * @param signaturesMatch      True when both fingerprints are non-null and equal.
 */
@Composable
fun AppSignatureCard(
    currentPackage: String,
    currentFingerprint: String?,
    targetPackage: String,
    targetFingerprint: String?,
    signaturesMatch: Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Signing Certificate Fingerprint",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Signature permissions are only granted when BOTH apps share the same signing cert.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(12.dp))

            FingerprintRow(
                label = currentPackage,
                fingerprint = currentFingerprint,
                onCopy = { fp -> context.copyToClipboard("Current app SHA-256", fp) },
            )
            Spacer(modifier = Modifier.height(8.dp))
            FingerprintRow(
                label = targetPackage,
                fingerprint = targetFingerprint ?: "(not installed)",
                onCopy = if (targetFingerprint != null) {
                    { fp -> context.copyToClipboard("Target app SHA-256", fp) }
                } else null,
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Match indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    imageVector = if (signaturesMatch) Icons.Default.CheckCircle else Icons.Default.Error,
                    contentDescription = null,
                    tint = if (signaturesMatch) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(18.dp),
                )
                Text(
                    text = if (signaturesMatch)
                        "Signatures match — INTER_APP_COMM will be auto-granted"
                    else
                        "Signatures differ — signature permission will be DENIED",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (signaturesMatch) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
private fun FingerprintRow(
    label: String,
    fingerprint: String?,
    onCopy: ((String) -> Unit)?,
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = fingerprint ?: "—",
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            if (onCopy != null && fingerprint != null) {
                IconButton(
                    onClick = { onCopy(fingerprint) },
                    modifier = Modifier.size(32.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy fingerprint",
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}

private fun Context.copyToClipboard(label: String, text: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun AppSignatureCardMatchPreview() {
    PreviewContainer {
        AppSignatureCard(
            currentPackage = "com.example.android.playground",
            currentFingerprint = "A1:B2:C3:D4:E5:F6:A1:B2:C3:D4:E5:F6:A1:B2:C3:D4",
            targetPackage = "com.example.android.playground.variant",
            targetFingerprint = "A1:B2:C3:D4:E5:F6:A1:B2:C3:D4:E5:F6:A1:B2:C3:D4",
            signaturesMatch = true,
        )
    }
}

@ComponentPreview
@Composable
private fun AppSignatureCardNotInstalledPreview() {
    PreviewContainer {
        AppSignatureCard(
            currentPackage = "com.example.android.playground",
            currentFingerprint = "A1:B2:C3:D4:E5:F6:A1:B2:C3:D4:E5:F6:A1:B2:C3:D4",
            targetPackage = "com.example.android.playground.variant",
            targetFingerprint = null,
            signaturesMatch = false,
        )
    }
}
