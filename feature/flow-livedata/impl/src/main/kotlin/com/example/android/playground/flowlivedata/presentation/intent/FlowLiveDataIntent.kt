package com.example.android.playground.flowlivedata.presentation.intent

import com.example.android.playground.flowlivedata.presentation.model.StreamType

sealed interface FlowLiveDataIntent {
    data class SelectTab(
        val tab: StreamType,
    ) : FlowLiveDataIntent

    data object ToggleEmitting : FlowLiveDataIntent

    data object ClearLog : FlowLiveDataIntent

    /**
     * StateFlow tab: starts a new collector to show that StateFlow immediately
     * replays the current value to any late subscriber (built-in replay=1).
     */
    data object SimulateLateSubscriber : FlowLiveDataIntent

    /**
     * SharedFlow tab: starts/stops Collector B to demonstrate that
     * SharedFlow(replay=0) permanently drops emissions for absent collectors.
     */
    data object ToggleCollectorB : FlowLiveDataIntent

    data object NavigateBack : FlowLiveDataIntent
}
