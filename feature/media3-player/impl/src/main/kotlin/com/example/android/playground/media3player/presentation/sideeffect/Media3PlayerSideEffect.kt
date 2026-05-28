package com.example.android.playground.media3player.presentation.sideeffect

sealed interface Media3PlayerSideEffect {
    data object NavigateBack : Media3PlayerSideEffect

    data class ShowError(
        val message: String,
    ) : Media3PlayerSideEffect
}
