package com.example.android.playground.flowlivedata.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
                    StreamType.STATE_FLOW ->
                        CurrentValueCard(
                            label = "Current StateFlow Value",
                            value = state.stateFlowValue,
                            subtitle = "Replays this value to every new collector instantly",
                        )
                    StreamType.SHARED_FLOW ->
                        EmissionLogCard(
                            title = "SharedFlow Events",
                            entries = state.sharedFlowLog,
                            onClear = { onIntent(FlowLiveDataIntent.ClearLog) },
                        )
                    StreamType.LIVE_DATA ->
                        CurrentValueCard(
                            label = "Current LiveData Value",
                            value = state.liveDataValue,
                            subtitle = "Only delivered while the observer lifecycle is STARTED",
                        )
                    StreamType.CHANNEL ->
                        EmissionLogCard(
                            title = "Channel Events",
                            entries = state.channelLog,
                            onClear = { onIntent(FlowLiveDataIntent.ClearLog) },
                        )
                }

                EmitControlRow(
                    isEmitting = state.isEmitting,
                    collectorCount = state.collectorCount,
                    showCollectorControl = state.selectedTab == StreamType.SHARED_FLOW,
                    onToggleEmitting = { onIntent(FlowLiveDataIntent.ToggleEmitting) },
                    onAddCollector = { onIntent(FlowLiveDataIntent.AddCollector) },
                    onRemoveCollector = { onIntent(FlowLiveDataIntent.RemoveCollector) },
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
            state = FlowLiveDataState(selectedTab = StreamType.STATE_FLOW, stateFlowValue = 7),
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
                    sharedFlowLog = listOf("#1 — 10:00:01", "#2 — 10:00:02"),
                    collectorCount = 2,
                ),
            onIntent = {},
        )
    }
}
