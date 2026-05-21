package com.example.android.playground.flowlivedata.presentation.state

import com.example.android.playground.flowlivedata.presentation.model.StreamType

data class FlowLiveDataState(
    val selectedTab: StreamType = StreamType.STATE_FLOW,
    val stateFlowValue: Int = 0,
    val sharedFlowLog: List<String> = emptyList(),
    val liveDataValue: Int = 0,
    val channelLog: List<String> = emptyList(),
    val isEmitting: Boolean = false,
    val collectorCount: Int = 1,
)
