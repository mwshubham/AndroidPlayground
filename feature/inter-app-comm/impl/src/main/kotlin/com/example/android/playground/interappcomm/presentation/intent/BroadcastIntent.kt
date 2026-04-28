package com.example.android.playground.interappcomm.presentation.intent

sealed interface BroadcastIntent {
    data object LoadData : BroadcastIntent
    data class OnInputChanged(val text: String) : BroadcastIntent
    data object SendBroadcast : BroadcastIntent
    data object ClearMessages : BroadcastIntent
    data object NavigateBack : BroadcastIntent
}
