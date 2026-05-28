package com.example.android.playground.websocket.domain.repository

import com.example.android.playground.websocket.domain.model.WebSocketEvent
import kotlinx.coroutines.flow.Flow

interface WebSocketRepository {
    fun observeTicker(): Flow<WebSocketEvent>
}
