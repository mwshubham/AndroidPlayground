package com.example.android.playground.sse.data.repository

import com.example.android.playground.sse.data.source.WikipediaEventSourceDataSource
import com.example.android.playground.sse.domain.model.SseEvent
import com.example.android.playground.sse.domain.repository.SseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SseRepositoryImpl
    @Inject
    constructor(
        private val source: WikipediaEventSourceDataSource,
    ) : SseRepository {
        override fun observeChanges(): Flow<SseEvent> = source.observeChanges()
    }
