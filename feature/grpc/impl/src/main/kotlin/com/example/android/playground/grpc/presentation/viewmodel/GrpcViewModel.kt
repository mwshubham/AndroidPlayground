package com.example.android.playground.grpc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.grpc.domain.model.MessageRole
import com.example.android.playground.grpc.domain.usecase.SendElizaMessageUseCase
import com.example.android.playground.grpc.presentation.intent.GrpcIntent
import com.example.android.playground.grpc.presentation.sideeffect.GrpcSideEffect
import com.example.android.playground.grpc.presentation.state.ElizaMessageUiModel
import com.example.android.playground.grpc.presentation.state.GrpcState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GrpcViewModel
    @Inject
    constructor(
        private val sendElizaMessageUseCase: SendElizaMessageUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(GrpcState())
        val state: StateFlow<GrpcState> = _state.asStateFlow()

        private val _sideEffects = Channel<GrpcSideEffect>()
        val sideEffects = _sideEffects.receiveAsFlow()

        fun processIntent(intent: GrpcIntent) {
            when (intent) {
                is GrpcIntent.UpdateInput -> _state.update { it.copy(inputText = intent.text) }
                is GrpcIntent.SendMessage -> sendMessage(intent.text)
            }
        }

        private fun sendMessage(text: String) {
            val trimmed = text.trim()
            if (trimmed.isBlank() || _state.value.isLoading) return

            val userMessage = ElizaMessageUiModel(text = trimmed, role = MessageRole.USER)
            _state.update {
                it.copy(
                    messages = it.messages + userMessage,
                    inputText = "",
                    isLoading = true,
                )
            }
            _sideEffects.trySend(GrpcSideEffect.ScrollToBottom)

            viewModelScope.launch {
                val reply = sendElizaMessageUseCase(trimmed)
                val elizaMessage = ElizaMessageUiModel(text = reply, role = MessageRole.ELIZA)
                _state.update {
                    it.copy(
                        messages = it.messages + elizaMessage,
                        isLoading = false,
                    )
                }
                _sideEffects.trySend(GrpcSideEffect.ScrollToBottom)
            }
        }
    }
