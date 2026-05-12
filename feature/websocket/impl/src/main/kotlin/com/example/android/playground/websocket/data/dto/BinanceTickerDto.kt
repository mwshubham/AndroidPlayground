package com.example.android.playground.websocket.data.dto

import com.example.android.playground.websocket.domain.model.BtcTicker
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BinanceTickerDto(
    @SerialName("e") val eventType: String = "",
    @SerialName("s") val symbol: String = "",
    @SerialName("c") val lastPrice: String = "0",
    @SerialName("p") val priceChange: String = "0",
    @SerialName("P") val priceChangePercent: String = "0",
    @SerialName("h") val highPrice: String = "0",
    @SerialName("l") val lowPrice: String = "0",
    @SerialName("v") val volume: String = "0",
    @SerialName("E") val eventTimeMs: Long = 0L,
) {
    fun toDomain(): BtcTicker =
        BtcTicker(
            symbol = symbol,
            lastPrice = lastPrice,
            priceChange = priceChange,
            priceChangePercent = priceChangePercent,
            highPrice = highPrice,
            lowPrice = lowPrice,
            volume = volume,
            eventTimeMs = eventTimeMs,
        )
}
