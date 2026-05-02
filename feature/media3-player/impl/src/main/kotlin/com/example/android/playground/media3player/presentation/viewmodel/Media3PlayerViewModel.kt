package com.example.android.playground.media3player.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.android.playground.media3player.domain.model.VideoItem
import com.example.android.playground.media3player.domain.usecase.GetVideosUseCase
import com.example.android.playground.media3player.presentation.intent.Media3PlayerIntent
import com.example.android.playground.media3player.presentation.model.VideoUiModel
import com.example.android.playground.media3player.presentation.sideeffect.Media3PlayerSideEffect
import com.example.android.playground.media3player.presentation.state.Media3PlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Media3PlayerViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val getVideosUseCase: GetVideosUseCase,
) : ViewModel(), Player.Listener {

    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()
    val player: ExoPlayer get() = exoPlayer

    private val _state = MutableStateFlow(Media3PlayerState())
    val state: StateFlow<Media3PlayerState> = _state.asStateFlow()

    private val _sideEffect = Channel<Media3PlayerSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<Media3PlayerSideEffect> = _sideEffect.receiveAsFlow()

    init {
        exoPlayer.addListener(this)
        viewModelScope.launch { loadVideos() }
        startPositionUpdates()
    }

    private suspend fun loadVideos() {
        _state.update { it.copy(isLoading = true, error = null) }
        runCatching { getVideosUseCase() }
            .onSuccess { videos ->
                val uiModels = videos.map { it.toUiModel() }
                _state.update { it.copy(isLoading = false, videos = uiModels) }
                if (uiModels.isNotEmpty()) {
                    prepareVideo(uiModels[0])
                    exoPlayer.play()
                }
            }
            .onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
                _sideEffect.send(Media3PlayerSideEffect.ShowError(error.message ?: "Failed to load videos"))
            }
    }

    private fun prepareVideo(video: VideoUiModel) {
        val mediaItem = buildMediaItem(video)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    private fun buildMediaItem(video: VideoUiModel): MediaItem {
        val builder = MediaItem.Builder()
            .setUri(video.dashUrl)
            .setMimeType(MimeTypes.APPLICATION_MPD)
        if (video.drmLicenseUrl != null) {
            val drmConfig = MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID)
                .setLicenseUri(video.drmLicenseUrl)
                .build()
            builder.setDrmConfiguration(drmConfig)
        }
        return builder.build()
    }

    fun handleIntent(intent: Media3PlayerIntent) {
        when (intent) {
            Media3PlayerIntent.TogglePlayPause -> {
                if (exoPlayer.isPlaying) {
                    exoPlayer.pause()
                } else {
                    if (exoPlayer.playbackState == Player.STATE_ENDED) exoPlayer.seekTo(0)
                    exoPlayer.play()
                }
            }
            Media3PlayerIntent.ToggleMute -> {
                val muted = !_state.value.isMuted
                exoPlayer.volume = if (muted) 0f else 1f
                _state.update { it.copy(isMuted = muted) }
            }
            is Media3PlayerIntent.SeekTo -> exoPlayer.seekTo(intent.positionMs)
            is Media3PlayerIntent.SelectVideo -> selectVideo(intent.index)
            Media3PlayerIntent.NavigateBack -> {
                viewModelScope.launch { _sideEffect.send(Media3PlayerSideEffect.NavigateBack) }
            }
            Media3PlayerIntent.OnLifecyclePause -> exoPlayer.pause()
            Media3PlayerIntent.OnLifecycleResume -> {
                // Only resume if we were playing before the lifecycle pause
                if (exoPlayer.playbackState == Player.STATE_READY && !exoPlayer.isPlaying) {
                    exoPlayer.play()
                }
            }
            is Media3PlayerIntent.OnPlayerError -> {
                _state.update { it.copy(error = intent.message) }
                viewModelScope.launch {
                    _sideEffect.send(Media3PlayerSideEffect.ShowError(intent.message))
                }
            }
            is Media3PlayerIntent.OnPositionChanged -> {
                _state.update {
                    it.copy(
                        currentPositionMs = intent.positionMs,
                        durationMs = intent.durationMs,
                    )
                }
            }
        }
    }

    private fun selectVideo(index: Int) {
        val currentState = _state.value
        val videos = currentState.videos
        if (index < 0 || index >= videos.size) return
        // Guard: don't re-prepare if the same video is already loaded and playing/buffering
        if (index == currentState.selectedVideoIndex && exoPlayer.playbackState != Player.STATE_IDLE) return
        _state.update { it.copy(selectedVideoIndex = index, currentPositionMs = 0L, durationMs = 0L, error = null) }
        prepareVideo(videos[index])
        exoPlayer.play()
    }

    // Player.Listener callbacks — push player events into state
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _state.update { it.copy(isPlaying = isPlaying) }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        val isBuffering = playbackState == Player.STATE_BUFFERING
        val duration = if (playbackState == Player.STATE_READY) exoPlayer.duration.coerceAtLeast(0L) else _state.value.durationMs
        _state.update { it.copy(isLoading = isBuffering, durationMs = duration) }
    }

    override fun onPlayerError(error: PlaybackException) {
        val message = error.message ?: "Playback error"
        _state.update { it.copy(error = message) }
        viewModelScope.launch { _sideEffect.send(Media3PlayerSideEffect.ShowError(message)) }
    }

    private fun startPositionUpdates() {
        viewModelScope.launch {
            while (true) {
                delay(POSITION_UPDATE_INTERVAL_MS)
                if (exoPlayer.isPlaying) {
                    _state.update {
                        it.copy(
                            currentPositionMs = exoPlayer.currentPosition.coerceAtLeast(0L),
                            durationMs = exoPlayer.duration.coerceAtLeast(0L),
                        )
                    }
                }
            }
        }
    }

    override fun onCleared() {
        exoPlayer.removeListener(this)
        exoPlayer.release()
        super.onCleared()
    }

    private fun VideoItem.toUiModel() = VideoUiModel(
        id = id,
        title = title,
        dashUrl = dashUrl,
        drmLicenseUrl = drmLicenseUrl,
        description = description,
    )

    private companion object {
        const val POSITION_UPDATE_INTERVAL_MS = 500L
    }
}
