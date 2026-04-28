package com.example.android.playground.interappcomm.presentation.sideeffect

sealed interface BroadcastSideEffect {
    data object NavigateBack : BroadcastSideEffect
    data class ShowMessage(val message: String) : BroadcastSideEffect
}
