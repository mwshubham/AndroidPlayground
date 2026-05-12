package com.example.android.playground.sse.presentation.sideeffect

sealed interface SseSideEffect {
    data class ShowError(
        val message: String,
    ) : SseSideEffect
}
