package com.example.android.playground.deviceclassifier.presentation.state

import com.example.android.playground.deviceclassifier.domain.model.DeviceSpec
import com.example.android.playground.deviceclassifier.domain.model.DeviceTier

data class DeviceClassifierState(
    val actualSpec: DeviceSpec? = null,
    val simulatedRamMb: Long = DEFAULT_RAM_MB,
    val simulatedCpuCores: Int = DEFAULT_CPU_CORES,
    val effectiveTier: DeviceTier = DeviceTier.MEDIUM,
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    private companion object {
        const val DEFAULT_RAM_MB = 2_048L
        const val DEFAULT_CPU_CORES = 4
    }
}
