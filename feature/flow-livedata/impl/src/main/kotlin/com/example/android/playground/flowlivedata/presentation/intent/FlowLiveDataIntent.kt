package com.example.android.playground.flowlivedata.presentation.intent

import com.example.android.playground.flowlivedata.presentation.model.StreamType

sealed interface FlowLiveDataIntent {
    data class SelectTab(
        val tab: StreamType,
    ) : FlowLiveDataIntent

    data object ToggleEmitting : FlowLiveDataIntent

    data object ClearLog : FlowLiveDataIntent

    data object AddCollector : FlowLiveDataIntent

    data object RemoveCollector : FlowLiveDataIntent

    data object NavigateBack : FlowLiveDataIntent
}
