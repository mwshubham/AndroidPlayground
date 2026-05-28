package com.example.android.playground.userinitiatedservice.presentation.sideeffect

sealed interface UserInitiatedServiceSideEffect {
    data object NavigateBack : UserInitiatedServiceSideEffect

    data class ShowMessage(
        val message: String,
    ) : UserInitiatedServiceSideEffect
}
