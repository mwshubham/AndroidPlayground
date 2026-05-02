package com.example.android.playground.media3player.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.media3player.presentation.intent.Media3PlayerIntent
import com.example.android.playground.media3player.presentation.model.VideoUiModel
import kotlinx.coroutines.delay

private const val OVERLAY_AUTO_HIDE_MS = 3_000L
private const val CENTER_ICON_VISIBLE_MS = 800L

/**
 * A single full-screen video page (one item in the VerticalPager).
 *
 * - Tap anywhere → toggle play/pause + reset the 3-second overlay timer.
 * - Overlay auto-hides after 3 s; re-shows on tap.
 * - Mute/unmute button in the bottom overlay row.
 * - [ElegantSeekBar] at the very bottom of the overlay.
 */
@Composable
internal fun VideoPage(
    video: VideoUiModel,
    isCurrentPage: Boolean,
    isPlaying: Boolean,
    isMuted: Boolean,
    currentPositionMs: Long,
    durationMs: Long,
    player: ExoPlayer?,
    onIntent: (Media3PlayerIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Increment to restart the auto-hide timer (also shows the overlay)
    var autoHideTrigger by remember { mutableIntStateOf(0) }
    var isOverlayVisible by remember { mutableStateOf(true) }

    // Brief centered play/pause icon feedback
    var centerIconTrigger by remember { mutableIntStateOf(0) }
    var showCenterIcon by remember { mutableStateOf(false) }

    // Auto-hide overlay 3 s after each trigger
    LaunchedEffect(autoHideTrigger) {
        isOverlayVisible = true
        delay(OVERLAY_AUTO_HIDE_MS)
        isOverlayVisible = false
    }

    // Show overlay whenever this page becomes the active page
    LaunchedEffect(isCurrentPage) {
        if (isCurrentPage) autoHideTrigger++
    }

    // Brief centered icon after tap
    LaunchedEffect(centerIconTrigger) {
        if (centerIconTrigger > 0) {
            showCenterIcon = true
            delay(CENTER_ICON_VISIBLE_MS)
            showCenterIcon = false
        }
    }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) {
                    onIntent(Media3PlayerIntent.TogglePlayPause)
                    autoHideTrigger++
                    centerIconTrigger++
                },
    ) {
        // ── Player surface ───────────────────────────────────────────────────
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
            },
            update = { view -> view.player = player },
            modifier = Modifier.fillMaxSize(),
        )

        // ── Brief centered play/pause icon ───────────────────────────────────
        AnimatedVisibility(
            visible = showCenterIcon,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center),
        ) {
            Box(
                modifier =
                    Modifier
                        .size(72.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.45f),
                            shape = MaterialTheme.shapes.extraLarge,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp),
                )
            }
        }

        // ── DRM badge (top-right) ────────────────────────────────────────────
        if (video.drmLicenseUrl != null) {
            Row(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 56.dp, end = 16.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.55f),
                            shape = MaterialTheme.shapes.small,
                        ).padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Widevine DRM",
                    tint = Color.White,
                    modifier = Modifier.size(12.dp),
                )
                Text(
                    text = "DRM",
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }

        // ── Bottom overlay: gradient + title + controls + seek bar ───────────
        AnimatedVisibility(
            visible = isOverlayVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(
                            brush =
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.80f)),
                                ),
                        ).navigationBarsPadding()
                        .padding(bottom = 12.dp),
            ) {
                // Title + description + mute
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = video.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.size(2.dp))
                        Text(
                            text = video.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.80f),
                            maxLines = 2,
                        )
                    }
                    // Mute / Unmute
                    IconButton(onClick = { onIntent(Media3PlayerIntent.ToggleMute) }) {
                        Icon(
                            imageVector =
                                if (isMuted) {
                                    Icons.AutoMirrored.Filled.VolumeOff
                                } else {
                                    Icons.AutoMirrored.Filled.VolumeUp
                                },
                            contentDescription = if (isMuted) "Unmute" else "Mute",
                            tint = Color.White,
                        )
                    }
                }

                // Seek bar — positioned slightly inside horizontal edges to avoid
                // system back-gesture zones; touch target is 44dp tall, track is 3dp
                val seekProgress =
                    if (durationMs > 0L) {
                        (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
                    } else {
                        0f
                    }
                ElegantSeekBar(
                    progress = seekProgress,
                    onSeekFinish = { p ->
                        onIntent(Media3PlayerIntent.SeekTo((p * durationMs).toLong()))
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                )
            }
        }
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun VideoPageOverlayPreview() {
    PreviewContainer {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray),
        ) {
            // Static representation of the overlay
            Column(
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            brush =
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.80f)),
                                ),
                        ).padding(bottom = 12.dp),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Angel One (Widevine DRM)",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                        )
                        Text(
                            text = "Short animated film encoded with Widevine DRM. Uses DASH adaptive streaming.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.80f),
                            maxLines = 2,
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.AutoMirrored.Filled.VolumeUp, null, tint = Color.White)
                    }
                }
                ElegantSeekBar(
                    progress = 0.35f,
                    onSeekFinish = {},
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                )
            }
        }
    }
}
