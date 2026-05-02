package com.example.android.playground.media3player.domain.repository

import com.example.android.playground.media3player.domain.model.VideoItem

interface Media3PlayerRepository {
    suspend fun getVideos(): List<VideoItem>
}
