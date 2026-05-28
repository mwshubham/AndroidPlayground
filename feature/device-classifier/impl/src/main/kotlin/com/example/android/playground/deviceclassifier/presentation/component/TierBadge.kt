package com.example.android.playground.deviceclassifier.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.deviceclassifier.domain.model.DeviceTier

@Composable
fun TierBadge(
    tier: DeviceTier,
    modifier: Modifier = Modifier,
) {
    val (containerColor, labelColor, label) =
        when (tier) {
            DeviceTier.LOW -> Triple(TierColors.LOW_CONTAINER, TierColors.LOW_LABEL, "LOW")
            DeviceTier.MEDIUM -> Triple(TierColors.MEDIUM_CONTAINER, TierColors.MEDIUM_LABEL, "MEDIUM")
            DeviceTier.HIGH -> Triple(TierColors.HIGH_CONTAINER, TierColors.HIGH_LABEL, "HIGH")
        }

    SuggestionChip(
        onClick = {},
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        modifier = modifier,
        colors =
            SuggestionChipDefaults.suggestionChipColors(
                containerColor = containerColor,
                labelColor = labelColor,
            ),
    )
}

private object TierColors {
    private const val LOW_CONTAINER_HEX = 0xFFFFDAD6L
    private const val LOW_LABEL_HEX = 0xFF410002L
    private const val MEDIUM_CONTAINER_HEX = 0xFFFFECC2L
    private const val MEDIUM_LABEL_HEX = 0xFF3D2800L
    private const val HIGH_CONTAINER_HEX = 0xFFB7F0ADL
    private const val HIGH_LABEL_HEX = 0xFF002204L

    val LOW_CONTAINER = Color(LOW_CONTAINER_HEX)
    val LOW_LABEL = Color(LOW_LABEL_HEX)
    val MEDIUM_CONTAINER = Color(MEDIUM_CONTAINER_HEX)
    val MEDIUM_LABEL = Color(MEDIUM_LABEL_HEX)
    val HIGH_CONTAINER = Color(HIGH_CONTAINER_HEX)
    val HIGH_LABEL = Color(HIGH_LABEL_HEX)
}

@DualThemePreview
@Composable
private fun TierBadgePreview() {
    PreviewContainer {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement =
                androidx.compose.foundation.layout.Arrangement
                    .spacedBy(8.dp),
        ) {
            TierBadge(tier = DeviceTier.LOW)
            TierBadge(tier = DeviceTier.MEDIUM)
            TierBadge(tier = DeviceTier.HIGH)
        }
    }
}
