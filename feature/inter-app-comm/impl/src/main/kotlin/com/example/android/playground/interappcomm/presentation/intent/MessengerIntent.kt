package com.example.android.playground.interappcomm.presentation.intent

sealed interface MessengerIntent {
    data object Connect : MessengerIntent

    data object Disconnect : MessengerIntent

    data class OnInputChanged(
        val text: String,
    ) : MessengerIntent

    data object SendMessage : MessengerIntent

    data object NavigateBack : MessengerIntent
}
