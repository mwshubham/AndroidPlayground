package com.example.android.playground.websocket.presentation.sideeffect

sealed interface WebSocketSideEffect {
    data class ShowError(
        val message: String,
    ) : WebSocketSideEffect
}
