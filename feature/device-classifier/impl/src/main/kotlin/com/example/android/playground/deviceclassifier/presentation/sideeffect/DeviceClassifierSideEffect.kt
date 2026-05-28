package com.example.android.playground.deviceclassifier.presentation.sideeffect

sealed interface DeviceClassifierSideEffect {
    data class ShowError(
        val message: String,
    ) : DeviceClassifierSideEffect

    data object NavigateBack : DeviceClassifierSideEffect
}
