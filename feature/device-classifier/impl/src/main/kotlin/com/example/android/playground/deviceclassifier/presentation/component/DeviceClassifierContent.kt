package com.example.android.playground.deviceclassifier.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.deviceclassifier.domain.model.DeviceSpec
import com.example.android.playground.deviceclassifier.domain.model.DeviceTier
import com.example.android.playground.deviceclassifier.presentation.intent.DeviceClassifierIntent
import com.example.android.playground.deviceclassifier.presentation.state.DeviceClassifierState

@Composable
internal fun DeviceClassifierContent(
    state: DeviceClassifierState,
    onIntent: (DeviceClassifierIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Device Classifier",
                onNavigationClick = { onIntent(DeviceClassifierIntent.NavigateBack) },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        when {
            state.isLoading -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 24.dp),
                    )
                }
            }

            state.actualSpec != null -> {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "Actual Device",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    DeviceSpecCard(
                        spec = state.actualSpec,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    SimulationPanel(
                        simulatedRamMb = state.simulatedRamMb,
                        simulatedCpuCores = state.simulatedCpuCores,
                        effectiveTier = state.effectiveTier,
                        isResetEnabled =
                            state.simulatedRamMb != state.actualSpec.ramMb ||
                                state.simulatedCpuCores != state.actualSpec.cpuCores,
                        onIntent = onIntent,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    AdaptiveExamplePanel(
                        tier = state.effectiveTier,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun DeviceClassifierContentPreview() {
    PreviewContainer {
        DeviceClassifierContent(
            state =
                DeviceClassifierState(
                    actualSpec = DeviceSpec(ramMb = 6_144L, cpuCores = 8, apiLevel = 33),
                    simulatedRamMb = 6_144L,
                    simulatedCpuCores = 8,
                    effectiveTier = DeviceTier.HIGH,
                ),
            onIntent = {},
        )
    }
}
