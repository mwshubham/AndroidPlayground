package com.example.android.playground.websocket.data.repository

import com.example.android.playground.websocket.data.source.BinanceWebSocketSource
import com.example.android.playground.websocket.domain.model.WebSocketEvent
import com.example.android.playground.websocket.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WebSocketRepositoryImpl
    @Inject
    constructor(
        private val source: BinanceWebSocketSource,
    ) : WebSocketRepository {
        override fun observeTicker(): Flow<WebSocketEvent> = source.observeTicker()
    }
