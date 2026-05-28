package com.example.android.playground.interappcomm.presentation.sideeffect

sealed interface MessengerSideEffect {
    data object NavigateBack : MessengerSideEffect

    data class ShowMessage(
        val message: String,
    ) : MessengerSideEffect
}
