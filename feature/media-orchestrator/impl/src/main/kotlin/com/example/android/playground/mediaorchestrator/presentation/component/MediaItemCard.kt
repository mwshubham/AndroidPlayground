package com.example.android.playground.mediaorchestrator.presentation.component

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.playground.mediaorchestrator.domain.model.UploadStatus
import com.example.android.playground.mediaorchestrator.presentation.model.MediaItemUiModel

@Composable
fun MediaItemCard(
    item: MediaItemUiModel,
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
                StatusChip(status = item.status)
            }

            if (item.status == UploadStatus.IN_PROGRESS || item.status == UploadStatus.SUCCESS) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxWidth(),
                    color = if (item.status == UploadStatus.SUCCESS) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                )
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }

            if (item.status == UploadStatus.SUCCESS && item.remoteUrl != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = item.remoteUrl,
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "MediaItemCard - PENDING")
@Composable
private fun MediaItemCardPendingPreview() {
    MediaItemCard(
        item = MediaItemUiModel(
            id = "1",
            name = "photo_001.jpg",
            sizeDisplay = "3.2 MB",
            status = UploadStatus.PENDING,
            progress = 0f,
            remoteUrl = null,
        ),
    )
}

@Preview(showBackground = true, name = "MediaItemCard - UPLOADING")
@Composable
private fun MediaItemCardInProgressPreview() {
    MediaItemCard(
        item = MediaItemUiModel(
            id = "2",
            name = "video_clip.mp4",
            sizeDisplay = "48.7 MB",
            status = UploadStatus.IN_PROGRESS,
            progress = 0.6f,
            remoteUrl = null,
        ),
    )
}

@Preview(showBackground = true, name = "MediaItemCard - SUCCESS")
@Composable
private fun MediaItemCardSuccessPreview() {
    MediaItemCard(
        item = MediaItemUiModel(
            id = "3",
            name = "photo_002.png",
            sizeDisplay = "1.1 MB",
            status = UploadStatus.SUCCESS,
            progress = 1f,
            remoteUrl = "https://cdn.example.com/media/3.png",
        ),
    )
}

@Preview(showBackground = true, name = "MediaItemCard - FAILED")
@Composable
private fun MediaItemCardFailedPreview() {
    MediaItemCard(
        item = MediaItemUiModel(
            id = "4",
            name = "photo_003.jpg",
            sizeDisplay = "2.5 MB",
            status = UploadStatus.FAILED,
            progress = 0f,
            remoteUrl = null,
        ),
    )
}
