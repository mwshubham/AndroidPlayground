package com.example.android.playground.mediaorchestrator.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.playground.mediaorchestrator.domain.model.UploadStatus
import com.example.android.playground.mediaorchestrator.presentation.model.MediaItemUiModel
import com.example.android.playground.mediaorchestrator.presentation.state.MediaOrchestratorState
import com.example.android.playground.mediaorchestrator.presentation.state.WorkerStatus

@Composable
internal fun MediaOrchestratorContent(
    state: MediaOrchestratorState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        WorkerStatusBanner(workerStatus = state.workerStatus)
        Spacer(modifier = Modifier.height(12.dp))
        if (state.totalCount > 0) {
            SummaryRow(state = state)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { state.overallProgress },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items = state.items, key = { it.id }) { item ->
                MediaItemCard(item = item)
            }
            item { Spacer(modifier = Modifier.height(88.dp)) }
        }
    }
}

@Preview(showBackground = true, name = "MediaOrchestratorContent - Empty")
@Composable
private fun MediaOrchestratorContentEmptyPreview() {
    MediaOrchestratorContent(state = MediaOrchestratorState())
}

@Preview(showBackground = true, name = "MediaOrchestratorContent - Running")
@Composable
private fun MediaOrchestratorContentRunningPreview() {
    MediaOrchestratorContent(
        state =
            MediaOrchestratorState(
                workerStatus = WorkerStatus.RUNNING,
                totalCount = 3,
                inProgressCount = 1,
                successCount = 1,
                failedCount = 0,
                pendingCount = 1,
                overallProgress = 0.45f,
                items =
                    listOf(
                        MediaItemUiModel(id = "1", name = "photo_001.jpg", sizeDisplay = "3.2 MB", status = UploadStatus.SUCCESS, progress = 1f, remoteUrl = "https://cdn.example.com/media/1.jpg"),
                        MediaItemUiModel(id = "2", name = "video_clip.mp4", sizeDisplay = "48.7 MB", status = UploadStatus.IN_PROGRESS, progress = 0.35f, remoteUrl = null),
                        MediaItemUiModel(id = "3", name = "photo_002.png", sizeDisplay = "1.1 MB", status = UploadStatus.PENDING, progress = 0f, remoteUrl = null),
                    ),
            ),
    )
}

@Preview(showBackground = true, name = "MediaOrchestratorContent - Done")
@Composable
private fun MediaOrchestratorContentDonePreview() {
    MediaOrchestratorContent(
        state =
            MediaOrchestratorState(
                workerStatus = WorkerStatus.DONE,
                totalCount = 2,
                inProgressCount = 0,
                successCount = 1,
                failedCount = 1,
                pendingCount = 0,
                overallProgress = 1f,
                items =
                    listOf(
                        MediaItemUiModel(id = "1", name = "photo_001.jpg", sizeDisplay = "3.2 MB", status = UploadStatus.SUCCESS, progress = 1f, remoteUrl = "https://cdn.example.com/media/1.jpg"),
                        MediaItemUiModel(id = "2", name = "photo_002.png", sizeDisplay = "1.1 MB", status = UploadStatus.FAILED, progress = 0f, remoteUrl = null),
                    ),
            ),
    )
}
