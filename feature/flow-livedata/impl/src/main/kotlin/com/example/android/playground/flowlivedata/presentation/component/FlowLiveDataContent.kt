package com.example.android.playground.flowlivedata.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.flowlivedata.presentation.intent.FlowLiveDataIntent
import com.example.android.playground.flowlivedata.presentation.model.StreamType
import com.example.android.playground.flowlivedata.presentation.state.FlowLiveDataState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FlowLiveDataContent(
    state: FlowLiveDataState,
    onIntent: (FlowLiveDataIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = "Flow vs LiveData vs Channel",
                onNavigationClick = { onIntent(FlowLiveDataIntent.NavigateBack) },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
        ) {
            StreamTypeTabRow(
                selectedTab = state.selectedTab,
                onTabSelected = { onIntent(FlowLiveDataIntent.SelectTab(it)) },
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StreamPropertiesCard(streamType = state.selectedTab)

                when (state.selectedTab) {
                    StreamType.STATE_FLOW -> {
                        CurrentValueCard(
                            label = "Current StateFlow Value",
                            value = state.stateFlowValue,
                            subtitle = "Replays this value to every new collector instantly",
                        )
                        LateSubscriberCard(
                            result = state.lateSubscriberResult,
                            onSimulate = { onIntent(FlowLiveDataIntent.SimulateLateSubscriber) },
                        )
                    }
                    StreamType.SHARED_FLOW -> {
                        EmissionLogCard(
                            title = "Collector A — always active",
                            entries = state.collectorALog,
                            onClear = { onIntent(FlowLiveDataIntent.ClearLog) },
                        )
                        EmissionLogCard(
                            title = if (state.isCollectorBActive) "Collector B — active" else "Collector B — stopped (missing emissions)",
                            entries = state.collectorBLog,
                            onClear = { onIntent(FlowLiveDataIntent.ClearLog) },
                        )
                        if (state.isCollectorBActive) {
                            OutlinedButton(
                                onClick = { onIntent(FlowLiveDataIntent.ToggleCollectorB) },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text("Stop Collector B")
                            }
                        } else {
                            Button(
                                onClick = { onIntent(FlowLiveDataIntent.ToggleCollectorB) },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text("Start Collector B")
                            }
                        }
                    }
                    StreamType.LIVE_DATA ->
                        CurrentValueCard(
                            label = "Current LiveData Value",
                            value = state.liveDataValue,
                            subtitle = "Only delivered while the observer lifecycle is STARTED",
                        )
                    StreamType.CHANNEL -> {
                        Text(
                            text = "Pending in queue: ${state.channelPendingCount}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        EmissionLogCard(
                            title = "Channel Events (consumed)",
                            entries = state.channelLog,
                            onClear = { onIntent(FlowLiveDataIntent.ClearLog) },
                        )
                    }
                }

                EmitControlRow(
                    isEmitting = state.isEmitting,
                    onToggleEmitting = { onIntent(FlowLiveDataIntent.ToggleEmitting) },
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, name = "FlowLiveDataContent - StateFlow")
@Composable
private fun FlowLiveDataContentStateFlowPreview() {
    PreviewContainer {
        FlowLiveDataContent(
            state =
                FlowLiveDataState(
                    selectedTab = StreamType.STATE_FLOW,
                    stateFlowValue = 7,
                    lateSubscriberResult = "Late subscriber got: 7 at 10:00:05",
                ),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, name = "FlowLiveDataContent - SharedFlow")
@Composable
private fun FlowLiveDataContentSharedFlowPreview() {
    PreviewContainer {
        FlowLiveDataContent(
            state =
                FlowLiveDataState(
                    selectedTab = StreamType.SHARED_FLOW,
                    collectorALog = listOf("Collector A: #1 at 10:00:01", "Collector A: #2 at 10:00:02"),
                    collectorBLog = listOf("Collector B: #1 at 10:00:01"),
                    isCollectorBActive = true,
                ),
            onIntent = {},
        )
    }
}
