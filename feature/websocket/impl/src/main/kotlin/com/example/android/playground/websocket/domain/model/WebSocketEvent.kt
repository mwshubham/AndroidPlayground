package com.example.android.playground.websocket.domain.model

sealed interface WebSocketEvent {
    data object Connecting : WebSocketEvent

    data object Connected : WebSocketEvent

    data class Tick(
        val ticker: BtcTicker,
    ) : WebSocketEvent

    data class Disconnected(
        val reason: String = "",
    ) : WebSocketEvent

    data class Error(
        val message: String,
    ) : WebSocketEvent
}
