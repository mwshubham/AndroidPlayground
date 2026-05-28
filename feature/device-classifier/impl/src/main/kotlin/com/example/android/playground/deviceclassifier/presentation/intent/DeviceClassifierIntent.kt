package com.example.android.playground.deviceclassifier.presentation.intent

sealed interface DeviceClassifierIntent {
    data object LoadDeviceSpec : DeviceClassifierIntent

    data class UpdateSimulatedRam(
        val ramMb: Long,
    ) : DeviceClassifierIntent

    data class UpdateSimulatedCpuCores(
        val cores: Int,
    ) : DeviceClassifierIntent

    data object ResetToDeviceDefaults : DeviceClassifierIntent

    data object NavigateBack : DeviceClassifierIntent
}
