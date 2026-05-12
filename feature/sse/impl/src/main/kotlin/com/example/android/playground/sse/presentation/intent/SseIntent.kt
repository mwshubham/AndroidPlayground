package com.example.android.playground.sse.presentation.intent

sealed interface SseIntent {
    data object Subscribe : SseIntent

    data object Unsubscribe : SseIntent

    data object Clear : SseIntent
}
