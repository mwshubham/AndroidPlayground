package com.example.android.playground.mediaorchestrator.presentation.intent

sealed interface MediaOrchestratorIntent {
    data object PickAndEnqueue : MediaOrchestratorIntent

    data object ClearAll : MediaOrchestratorIntent

    data object NavigateBack : MediaOrchestratorIntent
}
