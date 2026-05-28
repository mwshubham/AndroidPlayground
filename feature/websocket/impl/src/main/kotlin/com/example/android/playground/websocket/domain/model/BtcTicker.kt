package com.example.android.playground.websocket.domain.model

data class BtcTicker(
    val symbol: String,
    val lastPrice: String,
    val priceChange: String,
    val priceChangePercent: String,
    val highPrice: String,
    val lowPrice: String,
    val volume: String,
    val eventTimeMs: Long,
)
