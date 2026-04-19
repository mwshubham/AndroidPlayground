package com.example.android.playground.cryptosecurity.presentation.sideeffect

sealed interface SecureNetworkDemoSideEffect {
    data class ShowError(val message: String) : SecureNetworkDemoSideEffect
    data object NavigateBack : SecureNetworkDemoSideEffect
}
