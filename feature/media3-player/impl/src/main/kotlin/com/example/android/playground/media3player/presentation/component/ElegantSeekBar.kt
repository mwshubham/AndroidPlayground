package com.example.android.playground.media3player.presentation.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer

/**
 * Seek bar that:
 * - Uses a large (44dp) touch target but renders a thin (3dp) track by default.
 * - Animates the track to 18dp and shows a thumb while the user drags.
 * - Excludes the first/last 40dp from drag-start recognition to avoid
 *   conflicting with the system back-gesture zones on Android 14+.
 */
@Composable
internal fun ElegantSeekBar(
    progress: Float,
    onSeekFinish: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isDragging by remember { mutableStateOf(false) }
    var dragProgress by remember { mutableFloatStateOf(progress) }
    // Internal flag: drag only counts if it started outside the edge-exclusion zone
    var validDrag by remember { mutableStateOf(false) }

    val trackThickness by animateDpAsState(
        targetValue = if (isDragging) 18.dp else 3.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "seekBarThickness",
    )

    val displayProgress = if (isDragging) dragProgress else progress.coerceIn(0f, 1f)

    Canvas(
        modifier = modifier
            // 44dp height gives a comfortable touch target while the visual track stays thin
            .height(44.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { offset ->
                        // Leave 40dp on each edge free for the system back gesture
                        val edgePx = 40.dp.toPx()
                        validDrag = offset.x > edgePx && offset.x < size.width - edgePx
                        if (validDrag) {
                            isDragging = true
                            dragProgress = (offset.x / size.width).coerceIn(0f, 1f)
                        }
                    },
                    onHorizontalDrag = { change, _ ->
                        if (validDrag) {
                            change.consume()
                            dragProgress = (change.position.x / size.width).coerceIn(0f, 1f)
                        }
                    },
                    onDragEnd = {
                        if (validDrag) {
                            isDragging = false
                            validDrag = false
                            onSeekFinish(dragProgress)
                        }
                    },
                    onDragCancel = {
                        isDragging = false
                        validDrag = false
                    },
                )
            },
    ) {
        val centerY = size.height / 2f
        val trackH = trackThickness.toPx()
        val radius = CornerRadius(trackH / 2f)

        // Background (unplayed portion)
        drawRoundRect(
            color = Color.White.copy(alpha = 0.35f),
            topLeft = Offset(0f, centerY - trackH / 2f),
            size = Size(size.width, trackH),
            cornerRadius = radius,
        )

        // Played portion
        val playedWidth = (size.width * displayProgress).coerceAtLeast(0f)
        if (playedWidth > 0f) {
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(0f, centerY - trackH / 2f),
                size = Size(playedWidth, trackH),
                cornerRadius = radius,
            )
        }

        // Thumb — only visible while dragging
        if (isDragging) {
            drawCircle(
                color = Color.White,
                radius = trackH * 0.9f,
                center = Offset(playedWidth, centerY),
            )
        }
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun ElegantSeekBarIdlePreview() {
    PreviewContainer {
        ElegantSeekBar(
            progress = 0.4f,
            onSeekFinish = {},
        )
    }
}
