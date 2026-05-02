package com.example.android.playground.mediaorchestrator.presentation.sideeffect

sealed interface MediaOrchestratorSideEffect {
    data object NavigateBack : MediaOrchestratorSideEffect

    data class ShowMessage(
        val message: String,
    ) : MediaOrchestratorSideEffect
}
