package com.example.android.playground.sse.presentation.state

data class SseState(
    val connectionStatus: SseConnectionStatus = SseConnectionStatus.Disconnected,
    val changes: List<WikipediaChangeUiModel> = emptyList(),
)

enum class SseConnectionStatus {
    Disconnected,
    Connecting,
    Connected,
    Error,
}

data class WikipediaChangeUiModel(
    val title: String,
    val user: String,
    val wiki: String,
    val type: String,
    val comment: String,
    val formattedTime: String,
)
