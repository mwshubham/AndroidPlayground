package com.example.android.playground.deviceclassifier.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.deviceclassifier.domain.model.DeviceTier
import com.example.android.playground.deviceclassifier.presentation.intent.DeviceClassifierIntent

@Composable
fun SimulationPanel(
    simulatedRamMb: Long,
    simulatedCpuCores: Int,
    effectiveTier: DeviceTier,
    isResetEnabled: Boolean,
    onIntent: (DeviceClassifierIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Simulate Parameters",
                    style = MaterialTheme.typography.titleSmall,
                )
                TierBadge(tier = effectiveTier)
            }

            DeviceParamSlider(
                label = "RAM",
                valueDisplay = formatRam(simulatedRamMb),
                value = simulatedRamMb.toFloat(),
                valueRange = RAM_MIN_MB.toFloat()..RAM_MAX_MB.toFloat(),
                onValueChange = { onIntent(DeviceClassifierIntent.UpdateSimulatedRam(it.toLong())) },
            )

            DeviceParamSlider(
                label = "CPU Cores",
                valueDisplay = simulatedCpuCores.toString(),
                value = simulatedCpuCores.toFloat(),
                valueRange = CPU_MIN.toFloat()..CPU_MAX.toFloat(),
                steps = CPU_SLIDER_STEPS,
                onValueChange = { onIntent(DeviceClassifierIntent.UpdateSimulatedCpuCores(it.toInt())) },
            )

            if (isResetEnabled) {
                TextButton(
                    onClick = { onIntent(DeviceClassifierIntent.ResetToDeviceDefaults) },
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text(text = "Reset to device")
                }
            }
        }
    }
}

private fun formatRam(ramMb: Long): String = if (ramMb >= MB_PER_GB) "%.1f GB".format(ramMb / MB_PER_GB.toDouble()) else "$ramMb MB"

private const val MB_PER_GB = 1_024L
private const val RAM_MIN_MB = 512
private const val RAM_MAX_MB = 12_288
private const val CPU_MIN = 1
private const val CPU_MAX = 16
private const val CPU_SLIDER_STEPS = CPU_MAX - CPU_MIN - 1

@DualThemePreview
@Composable
private fun SimulationPanelPreview() {
    PreviewContainer {
        SimulationPanel(
            simulatedRamMb = 3_072L,
            simulatedCpuCores = 6,
            effectiveTier = DeviceTier.MEDIUM,
            isResetEnabled = true,
            onIntent = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
