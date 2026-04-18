package com.example.android.playground.userinitiatedservice.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.playground.userinitiatedservice.domain.model.TransferStatus
import com.example.android.playground.userinitiatedservice.presentation.model.TransferItemUiModel

@Composable
fun TransferItemCard(
    item: TransferItemUiModel,
    modifier: Modifier = Modifier,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = item.progress,
        label = "chunk_progress_${item.id}",
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = item.sizeDisplay,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                SuggestionChip(
                    onClick = {},
                    label = {
                        Text(
                            text = item.status.name,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    },
                    colors =
                        SuggestionChipDefaults.suggestionChipColors(
                            containerColor = chipContainerColor(item.status),
                            labelColor = chipLabelColor(item.status),
                        ),
                )
            }

            if (item.status == TransferStatus.RUNNING || item.progress > 0f) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxWidth(),
                    color =
                        when (item.status) {
                            TransferStatus.SUCCESS -> Color(0xFF2E7D32)
                            TransferStatus.FAILED -> MaterialTheme.colorScheme.error
                            TransferStatus.CANCELLED -> Color(0xFFBF360C)
                            else -> MaterialTheme.colorScheme.primary
                        },
                )
            }
        }
    }
}

@Composable
private fun chipContainerColor(status: TransferStatus): Color =
    when (status) {
        TransferStatus.PENDING -> MaterialTheme.colorScheme.surfaceVariant
        TransferStatus.RUNNING -> MaterialTheme.colorScheme.primaryContainer
        TransferStatus.SUCCESS -> Color(0xFF1B5E20).copy(alpha = 0.15f)
        TransferStatus.FAILED -> MaterialTheme.colorScheme.errorContainer
        TransferStatus.CANCELLED -> Color(0xFFE65100).copy(alpha = 0.15f)
    }

@Composable
private fun chipLabelColor(status: TransferStatus): Color =
    when (status) {
        TransferStatus.PENDING -> MaterialTheme.colorScheme.onSurfaceVariant
        TransferStatus.RUNNING -> MaterialTheme.colorScheme.onPrimaryContainer
        TransferStatus.SUCCESS -> Color(0xFF2E7D32)
        TransferStatus.FAILED -> MaterialTheme.colorScheme.onErrorContainer
        TransferStatus.CANCELLED -> Color(0xFFBF360C)
    }

// ---- Previews ----

private fun previewItem(status: TransferStatus, progress: Float) = TransferItemUiModel(
    id = status.name,
    name = "example-file-${status.name.lowercase()}.zip",
    sizeDisplay = "42 MB",
    status = status,
    progress = progress,
)

@Preview(showBackground = true, name = "TransferItemCard — Pending")
@Composable
private fun TransferItemCardPendingPreview() {
    TransferItemCard(item = previewItem(TransferStatus.PENDING, 0f))
}

@Preview(showBackground = true, name = "TransferItemCard — Running")
@Composable
private fun TransferItemCardRunningPreview() {
    TransferItemCard(item = previewItem(TransferStatus.RUNNING, 0.45f))
}

@Preview(showBackground = true, name = "TransferItemCard — Success")
@Composable
private fun TransferItemCardSuccessPreview() {
    TransferItemCard(item = previewItem(TransferStatus.SUCCESS, 1f))
}

@Preview(showBackground = true, name = "TransferItemCard — Failed")
@Composable
private fun TransferItemCardFailedPreview() {
    TransferItemCard(item = previewItem(TransferStatus.FAILED, 0.6f))
}

@Preview(showBackground = true, name = "TransferItemCard — Cancelled")
@Composable
private fun TransferItemCardCancelledPreview() {
    TransferItemCard(item = previewItem(TransferStatus.CANCELLED, 0.3f))
}

