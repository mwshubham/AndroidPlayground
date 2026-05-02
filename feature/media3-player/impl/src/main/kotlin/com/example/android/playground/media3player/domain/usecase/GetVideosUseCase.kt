package com.example.android.playground.media3player.domain.usecase

import com.example.android.playground.media3player.domain.model.VideoItem
import com.example.android.playground.media3player.domain.repository.Media3PlayerRepository
import javax.inject.Inject

class GetVideosUseCase
    @Inject
    constructor(
        private val repository: Media3PlayerRepository,
    ) {
        suspend operator fun invoke(): List<VideoItem> = repository.getVideos()
    }
