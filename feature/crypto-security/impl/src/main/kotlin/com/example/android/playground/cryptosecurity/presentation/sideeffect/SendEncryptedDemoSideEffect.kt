package com.example.android.playground.cryptosecurity.presentation.sideeffect

sealed interface SendEncryptedDemoSideEffect {
    data class ShowError(
        val message: String,
    ) : SendEncryptedDemoSideEffect

    data object NavigateBack : SendEncryptedDemoSideEffect
}
