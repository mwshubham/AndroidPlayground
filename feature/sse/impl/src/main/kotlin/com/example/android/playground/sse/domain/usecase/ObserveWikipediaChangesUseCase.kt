package com.example.android.playground.sse.domain.usecase

import com.example.android.playground.sse.domain.model.SseEvent
import com.example.android.playground.sse.domain.repository.SseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWikipediaChangesUseCase
    @Inject
    constructor(
        private val repository: SseRepository,
    ) {
        operator fun invoke(): Flow<SseEvent> = repository.observeChanges()
    }
