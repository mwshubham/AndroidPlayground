package com.example.android.playground.flowlivedata.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.flowlivedata.presentation.model.StreamType

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun StreamPropertiesCard(
    streamType: StreamType,
    modifier: Modifier = Modifier,
) {
    val properties = streamType.properties()
    val note = streamType.note()

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Key Properties",
                style = MaterialTheme.typography.titleSmall,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                properties.forEach { property ->
                    AssistChip(
                        onClick = {},
                        label = { Text(text = property, style = MaterialTheme.typography.labelSmall) },
                    )
                }
            }
            if (note != null) {
                Text(
                    text = note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private fun StreamType.properties(): List<String> =
    when (this) {
        StreamType.STATE_FLOW ->
            listOf(
                "Hot stream",
                "Replays last value",
                "Requires initial value",
                "Conflates fast emissions",
                "Lifecycle-agnostic",
                "Thread-safe",
            )
        StreamType.SHARED_FLOW ->
            listOf(
                "Hot stream",
                "No initial value",
                "Configurable replay cache",
                "Fan-out (multiple collectors)",
                "No conflation",
                "Lifecycle-agnostic",
            )
        StreamType.LIVE_DATA ->
            listOf(
                "Hot stream",
                "Lifecycle-aware",
                "Replays last value",
                "Main-thread delivery",
                "Auto-unsubscribes on STOP",
                "Java-friendly",
            )
        StreamType.CHANNEL ->
            listOf(
                "Cold-like point-to-point",
                "Single consumer",
                "Buffered queue",
                "No replay",
                "Suspends on full buffer",
                "Coroutine-native",
            )
    }

private fun StreamType.note(): String? =
    when (this) {
        StreamType.STATE_FLOW ->
            "Prefer over LiveData in new Kotlin-first code. Collect with collectAsStateWithLifecycle() in Compose."
        StreamType.SHARED_FLOW ->
            "Ideal for one-shot events (navigation, snackbars). Use replay=1 to get StateFlow-like caching."
        StreamType.LIVE_DATA ->
            "Legacy. Automatically stops delivery when the observer's lifecycle is below STARTED. Use observeAsState() in Compose."
        StreamType.CHANNEL ->
            "Used inside ViewModels for side effects (Channel.BUFFERED). Never expose a Channel directly — expose receiveAsFlow() instead."
    }

@DualThemePreview
@Composable
private fun StreamPropertiesCardPreview() {
    PreviewContainer {
        StreamPropertiesCard(streamType = StreamType.STATE_FLOW)
    }
}

@DualThemePreview
@Composable
private fun StreamPropertiesCardChannelPreview() {
    PreviewContainer {
        StreamPropertiesCard(streamType = StreamType.CHANNEL)
    }
}
