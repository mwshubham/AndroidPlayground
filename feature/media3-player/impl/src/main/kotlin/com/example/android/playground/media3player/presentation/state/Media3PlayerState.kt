package com.example.android.playground.media3player.presentation.state

import com.example.android.playground.media3player.presentation.model.VideoUiModel

data class Media3PlayerState(
    val isLoading: Boolean = true,
    val isPlaying: Boolean = false,
    val isMuted: Boolean = false,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val videos: List<VideoUiModel> = emptyList(),
    val selectedVideoIndex: Int = 0,
    val error: String? = null,
)
