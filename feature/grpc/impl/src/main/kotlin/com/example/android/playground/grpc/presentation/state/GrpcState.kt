package com.example.android.playground.grpc.presentation.state

import com.example.android.playground.grpc.domain.model.MessageRole

data class GrpcState(
    val messages: List<ElizaMessageUiModel> = emptyList(),
    val inputText: String = "",
    val isLoading: Boolean = false,
)

data class ElizaMessageUiModel(
    val text: String,
    val role: MessageRole,
)
