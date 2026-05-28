package com.example.android.playground.deviceclassifier.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.deviceclassifier.domain.model.DeviceTier

private val sampleItems = listOf("Item Alpha", "Item Beta", "Item Gamma", "Item Delta", "Item Epsilon")

@Composable
fun AdaptiveExamplePanel(
    tier: DeviceTier,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Adaptive UI — ${tier.name} tier",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        when (tier) {
            DeviceTier.LOW -> LowTierContent()
            DeviceTier.MEDIUM -> MediumTierContent()
            DeviceTier.HIGH -> HighTierContent()
        }
    }
}

/** No animation, flat rows, plain text — optimal for constrained devices. */
@Composable
private fun LowTierContent() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        sampleItems.forEach { item ->
            Text(
                text = item,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/** Fade-in animation + slight card elevation — moderate visual polish. */
@Composable
private fun MediumTierContent() {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            sampleItems.forEach { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp),
                    )
                }
            }
        }
    }
}

/** Spring animations + gradient background + icon decorations — full visual fidelity. */
@Composable
private fun HighTierContent() {
    var launched by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { launched = true }

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors =
                            listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.secondaryContainer,
                            ),
                    ),
                    RoundedCornerShape(8.dp),
                ).padding(12.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            sampleItems.forEachIndexed { index, item ->
                val scale by animateFloatAsState(
                    targetValue = if (launched) 1f else 0f,
                    animationSpec =
                        spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow,
                        ),
                    label = "item_scale_$index",
                )
                val alpha by animateFloatAsState(
                    targetValue = if (launched) 1f else 0f,
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                    label = "item_alpha_$index",
                )
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .scale(scale)
                            .alpha(alpha),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@DualThemePreview
@Composable
private fun AdaptiveExamplePanelLowPreview() {
    PreviewContainer {
        AdaptiveExamplePanel(
            tier = DeviceTier.LOW,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@DualThemePreview
@Composable
private fun AdaptiveExamplePanelMediumPreview() {
    PreviewContainer {
        AdaptiveExamplePanel(
            tier = DeviceTier.MEDIUM,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@DualThemePreview
@Composable
private fun AdaptiveExamplePanelHighPreview() {
    PreviewContainer {
        AdaptiveExamplePanel(
            tier = DeviceTier.HIGH,
            modifier = Modifier.padding(16.dp),
        )
    }
}
