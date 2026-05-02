package com.example.android.playground.interappcomm.presentation.sideeffect

sealed interface ContentProviderSideEffect {
    data object NavigateBack : ContentProviderSideEffect

    data class ShowMessage(
        val message: String,
    ) : ContentProviderSideEffect
}
