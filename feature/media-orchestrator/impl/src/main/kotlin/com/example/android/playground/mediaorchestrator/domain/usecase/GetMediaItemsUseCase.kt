package com.example.android.playground.mediaorchestrator.domain.usecase

import com.example.android.playground.mediaorchestrator.domain.model.MediaItem
import com.example.android.playground.mediaorchestrator.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaItemsUseCase
    @Inject
    constructor(
        private val repository: MediaRepository,
    ) {
        operator fun invoke(): Flow<List<MediaItem>> = repository.observeAll()
    }
