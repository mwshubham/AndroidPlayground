package com.example.android.playground.interappcomm.presentation.sideeffect

sealed interface AidlSideEffect {
    data object NavigateBack : AidlSideEffect
    data class ShowMessage(val message: String) : AidlSideEffect
}
