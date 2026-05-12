package com.example.android.playground.grpc.presentation.intent

sealed interface GrpcIntent {
    data class UpdateInput(
        val text: String,
    ) : GrpcIntent

    data class SendMessage(
        val text: String,
    ) : GrpcIntent
}
