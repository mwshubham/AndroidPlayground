package com.example.android.playground.flowlivedata.presentation.state

import com.example.android.playground.flowlivedata.presentation.model.StreamType

data class FlowLiveDataState(
    val selectedTab: StreamType = StreamType.STATE_FLOW,
    // StateFlow — holds latest value; replays to every new subscriber immediately
    val stateFlowValue: Int = 0,
    val lateSubscriberResult: String = "Tap 'Simulate Late Subscribe' to see replay",
    // SharedFlow(replay=0) — hot stream; Collector B can be stopped to reveal missed emissions
    val collectorALog: List<String> = emptyList(),
    val collectorBLog: List<String> = emptyList(),
    val isCollectorBActive: Boolean = false,
    // LiveData — lifecycle-aware; always delivers the last value to active observers
    val liveDataValue: Int = 0,
    // Channel — FIFO queue; each item consumed by exactly one receiver
    val channelLog: List<String> = emptyList(),
    val channelPendingCount: Int = 0,
    val isEmitting: Boolean = false,
)
