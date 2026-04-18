package com.example.android.playground.mediaorchestrator.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.playground.mediaorchestrator.domain.model.UploadStatus

@Composable
internal fun StatusChip(status: UploadStatus) {
    val (label, containerColor, labelColor) =
        when (status) {
            UploadStatus.PENDING ->
                Triple(
                    "PENDING",
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.colorScheme.onSurfaceVariant,
                )
            UploadStatus.IN_PROGRESS ->
                Triple(
                    "UPLOADING",
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.onPrimaryContainer,
                )
            UploadStatus.SUCCESS ->
                Triple(
                    "SUCCESS",
                    Color(0xFF1B5E20),
                    Color.White,
                )
            UploadStatus.FAILED ->
                Triple(
                    "FAILED",
                    MaterialTheme.colorScheme.errorContainer,
                    MaterialTheme.colorScheme.onErrorContainer,
                )
        }

    SuggestionChip(
        onClick = {},
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = labelColor,
            )
        },
        colors =
            SuggestionChipDefaults.suggestionChipColors(
                containerColor = containerColor,
            ),
    )
}

@Preview(showBackground = true, name = "StatusChip - PENDING")
@Composable
private fun StatusChipPendingPreview() {
    StatusChip(status = UploadStatus.PENDING)
}

@Preview(showBackground = true, name = "StatusChip - UPLOADING")
@Composable
private fun StatusChipInProgressPreview() {
    StatusChip(status = UploadStatus.IN_PROGRESS)
}

@Preview(showBackground = true, name = "StatusChip - SUCCESS")
@Composable
private fun StatusChipSuccessPreview() {
    StatusChip(status = UploadStatus.SUCCESS)
}

@Preview(showBackground = true, name = "StatusChip - FAILED")
@Composable
private fun StatusChipFailedPreview() {
    StatusChip(status = UploadStatus.FAILED)
}

