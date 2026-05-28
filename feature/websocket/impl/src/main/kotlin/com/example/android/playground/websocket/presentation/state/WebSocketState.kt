package com.example.android.playground.websocket.presentation.state

data class WebSocketState(
    val connectionStatus: WebSocketConnectionStatus = WebSocketConnectionStatus.Disconnected,
    val price: String = "--",
    val priceChange: String = "--",
    val priceChangePercent: String = "--",
    val highPrice: String = "--",
    val lowPrice: String = "--",
    val isPositive: Boolean = false,
    val recentTicks: List<BtcTickerUiModel> = emptyList(),
)

enum class WebSocketConnectionStatus {
    Disconnected,
    Connecting,
    Connected,
    Error,
}

data class BtcTickerUiModel(
    val price: String,
    val priceChange: String,
    val priceChangePercent: String,
    val isPositive: Boolean,
    val formattedTime: String,
)
