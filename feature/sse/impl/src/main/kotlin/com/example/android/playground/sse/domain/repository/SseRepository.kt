package com.example.android.playground.sse.domain.repository

import com.example.android.playground.sse.domain.model.SseEvent
import kotlinx.coroutines.flow.Flow

interface SseRepository {
    fun observeChanges(): Flow<SseEvent>
}
