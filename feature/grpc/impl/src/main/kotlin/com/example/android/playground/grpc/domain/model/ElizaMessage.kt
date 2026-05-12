package com.example.android.playground.grpc.domain.model

data class ElizaMessage(
    val role: MessageRole,
    val text: String,
)

enum class MessageRole {
    USER,
    ELIZA,
}
