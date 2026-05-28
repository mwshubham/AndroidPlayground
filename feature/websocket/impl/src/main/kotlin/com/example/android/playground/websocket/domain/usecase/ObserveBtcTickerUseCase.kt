package com.example.android.playground.websocket.domain.usecase

import com.example.android.playground.websocket.domain.model.WebSocketEvent
import com.example.android.playground.websocket.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveBtcTickerUseCase
    @Inject
    constructor(
        private val repository: WebSocketRepository,
    ) {
        operator fun invoke(): Flow<WebSocketEvent> = repository.observeTicker()
    }
