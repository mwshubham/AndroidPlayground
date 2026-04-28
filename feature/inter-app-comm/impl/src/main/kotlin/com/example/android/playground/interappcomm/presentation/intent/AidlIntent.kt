package com.example.android.playground.interappcomm.presentation.intent

sealed interface AidlIntent {
    data object Connect : AidlIntent
    data object Disconnect : AidlIntent
    data object Ping : AidlIntent
    data object GetMessages : AidlIntent
    data class OnInputChanged(val text: String) : AidlIntent
    data object PostMessage : AidlIntent
    data object NavigateBack : AidlIntent
}
