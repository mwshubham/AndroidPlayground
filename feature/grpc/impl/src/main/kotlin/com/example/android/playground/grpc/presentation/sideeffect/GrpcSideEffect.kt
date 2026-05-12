package com.example.android.playground.grpc.presentation.sideeffect

sealed interface GrpcSideEffect {
    data object ScrollToBottom : GrpcSideEffect

    data class ShowError(
        val message: String,
    ) : GrpcSideEffect
}
