package com.example.android.playground.flowlivedata.presentation.sideeffect

sealed interface FlowLiveDataSideEffect {
    data object NavigateBack : FlowLiveDataSideEffect
}
