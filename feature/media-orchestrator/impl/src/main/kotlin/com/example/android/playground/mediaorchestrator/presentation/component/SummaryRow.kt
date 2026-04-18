package com.example.android.playground.mediaorchestrator.presentation.component
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.playground.mediaorchestrator.presentation.state.MediaOrchestratorState
import com.example.android.playground.mediaorchestrator.presentation.state.WorkerStatus
@Composable
internal fun SummaryRow(
    state: MediaOrchestratorState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        SummaryLabel(label = "Total", value = state.totalCount.toString())
        SummaryLabel(label = "Uploading", value = state.inProgressCount.toString())
        SummaryLabel(label = "Done", value = state.successCount.toString())
        SummaryLabel(label = "Failed", value = state.failedCount.toString())
        SummaryLabel(label = "Queued", value = state.pendingCount.toString())
    }
}
@Preview(showBackground = true, name = "SummaryRow - all zeros")
@Composable
private fun SummaryRowEmptyPreview() {
    SummaryRow(state = MediaOrchestratorState())
}
@Preview(showBackground = true, name = "SummaryRow - in progress")
@Composable
private fun SummaryRowInProgressPreview() {
    SummaryRow(
        state = MediaOrchestratorState(
            workerStatus = WorkerStatus.RUNNING,
            totalCount = 10,
            inProgressCount = 3,
            successCount = 5,
            failedCount = 1,
            pendingCount = 1,
            overallProgress = 0.6f,
        ),
    )
}
