package com.example.android.playground.deviceclassifier.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.deviceclassifier.domain.model.DeviceSpec

@Composable
fun DeviceSpecCard(
    spec: DeviceSpec,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(modifier = modifier.fillMaxWidth()) {
        SpecRow(
            label = "RAM",
            value = formatRam(spec.ramMb),
        )
        SpecRow(
            label = "CPU Cores",
            value = spec.cpuCores.toString(),
        )
        SpecRow(
            label = "API Level",
            value = spec.apiLevel.toString(),
        )
    }
}

@Composable
private fun SpecRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

private fun formatRam(ramMb: Long): String =
    if (ramMb >= MB_PER_GB) {
        val gb = ramMb / MB_PER_GB.toDouble()
        "%.1f GB".format(gb)
    } else {
        "$ramMb MB"
    }

private const val MB_PER_GB = 1_024L

@DualThemePreview
@Composable
private fun DeviceSpecCardPreview() {
    PreviewContainer {
        DeviceSpecCard(
            spec = DeviceSpec(ramMb = 6_144L, cpuCores = 8, apiLevel = 33),
            modifier = Modifier.padding(16.dp),
        )
    }
}
