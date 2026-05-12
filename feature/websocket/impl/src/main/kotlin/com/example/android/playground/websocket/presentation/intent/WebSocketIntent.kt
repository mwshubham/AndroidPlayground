package com.example.android.playground.websocket.presentation.intent

sealed interface WebSocketIntent {
    data object Connect : WebSocketIntent

    data object Disconnect : WebSocketIntent
}
