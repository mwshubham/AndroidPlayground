package com.example.android.playground.media3player.presentation.intent

sealed interface Media3PlayerIntent {
    data object TogglePlayPause : Media3PlayerIntent

    data object ToggleMute : Media3PlayerIntent

    data class SeekTo(
        val positionMs: Long,
    ) : Media3PlayerIntent

    data class SelectVideo(
        val index: Int,
    ) : Media3PlayerIntent

    data object NavigateBack : Media3PlayerIntent

    data object OnLifecyclePause : Media3PlayerIntent

    data object OnLifecycleResume : Media3PlayerIntent

    data class OnPlayerError(
        val message: String,
    ) : Media3PlayerIntent

    data class OnPositionChanged(
        val positionMs: Long,
        val durationMs: Long,
    ) : Media3PlayerIntent
}
