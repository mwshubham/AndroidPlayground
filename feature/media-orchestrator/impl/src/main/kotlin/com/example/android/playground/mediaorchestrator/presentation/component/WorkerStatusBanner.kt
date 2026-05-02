package com.example.android.playground.mediaorchestrator.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.playground.mediaorchestrator.presentation.state.WorkerStatus

private const val SUCCESS_GREEN_ARGB = 0xFF1B5E20L
private val SuccessGreen = Color(SUCCESS_GREEN_ARGB)

@Composable
fun WorkerStatusBanner(
    workerStatus: WorkerStatus,
    modifier: Modifier = Modifier,
) {
    val (containerColor, text) =
        when (workerStatus) {
            WorkerStatus.IDLE -> Pair(MaterialTheme.colorScheme.surfaceVariant, "Idle — tap Pick & Upload to start")
            WorkerStatus.RUNNING -> Pair(MaterialTheme.colorScheme.primaryContainer, "Worker running — uploading in background")
            WorkerStatus.DONE -> Pair(SuccessGreen.copy(alpha = 0.15f), "Done — all items processed")
        }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (workerStatus == WorkerStatus.RUNNING) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            val textColor =
                when (workerStatus) {
                    WorkerStatus.IDLE -> MaterialTheme.colorScheme.onSurfaceVariant
                    WorkerStatus.RUNNING -> MaterialTheme.colorScheme.onPrimaryContainer
                    WorkerStatus.DONE -> SuccessGreen
                }
            Text(
                text = text,
                modifier = Modifier,
                color = textColor,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Preview(showBackground = true, name = "WorkerStatusBanner - IDLE")
@Composable
private fun WorkerStatusBannerIdlePreview() {
    WorkerStatusBanner(workerStatus = WorkerStatus.IDLE)
}

@Preview(showBackground = true, name = "WorkerStatusBanner - RUNNING")
@Composable
private fun WorkerStatusBannerRunningPreview() {
    WorkerStatusBanner(workerStatus = WorkerStatus.RUNNING)
}

@Preview(showBackground = true, name = "WorkerStatusBanner - DONE")
@Composable
private fun WorkerStatusBannerDonePreview() {
    WorkerStatusBanner(workerStatus = WorkerStatus.DONE)
}
