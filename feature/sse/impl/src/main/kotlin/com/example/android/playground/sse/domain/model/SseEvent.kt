package com.example.android.playground.sse.domain.model

sealed class SseEvent {
    data object Connecting : SseEvent()

    data object Connected : SseEvent()

    data class Change(
        val change: WikipediaChange,
    ) : SseEvent()

    data object Disconnected : SseEvent()

    data class Error(
        val message: String,
    ) : SseEvent()
}
