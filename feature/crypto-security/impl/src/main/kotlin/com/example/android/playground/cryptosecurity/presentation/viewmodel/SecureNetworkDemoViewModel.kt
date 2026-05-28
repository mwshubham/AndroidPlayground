package com.example.android.playground.cryptosecurity.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.cryptosecurity.domain.usecase.DecryptNetworkResponseUseCase
import com.example.android.playground.cryptosecurity.domain.usecase.FetchEncryptedFromServerUseCase
import com.example.android.playground.cryptosecurity.presentation.intent.SecureNetworkDemoIntent
import com.example.android.playground.cryptosecurity.presentation.sideeffect.SecureNetworkDemoSideEffect
import com.example.android.playground.cryptosecurity.presentation.state.SecureNetworkDemoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecureNetworkDemoViewModel
    @Inject
    constructor(
        private val fetchEncryptedFromServer: FetchEncryptedFromServerUseCase,
        private val decryptNetworkResponse: DecryptNetworkResponseUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(SecureNetworkDemoState())
        val state: StateFlow<SecureNetworkDemoState> = _state.asStateFlow()

        private val _sideEffect = Channel<SecureNetworkDemoSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        fun handleIntent(intent: SecureNetworkDemoIntent) {
            when (intent) {
                SecureNetworkDemoIntent.FetchData -> fetchData()
                SecureNetworkDemoIntent.DecryptPayload -> decryptPayload()
                SecureNetworkDemoIntent.Clear -> _state.value = SecureNetworkDemoState()
                SecureNetworkDemoIntent.NavigateBack -> viewModelScope.launch { _sideEffect.send(SecureNetworkDemoSideEffect.NavigateBack) }
            }
        }

        private fun fetchData() {
            viewModelScope.launch {
                _state.value = _state.value.copy(isLoading = true, error = null, decryptedMessage = null)
                runCatching { fetchEncryptedFromServer() }
                    .onSuccess { response ->
                        _state.value = _state.value.copy(isLoading = false, serverPayload = response)
                    }.onFailure { e ->
                        val msg = e.message ?: "Failed to fetch data"
                        _state.value = _state.value.copy(isLoading = false, error = msg)
                        _sideEffect.send(SecureNetworkDemoSideEffect.ShowError(msg))
                    }
            }
        }

        private fun decryptPayload() {
            val payload = _state.value.serverPayload ?: return
            viewModelScope.launch {
                runCatching { decryptNetworkResponse(payload) }
                    .onSuccess { plaintext ->
                        _state.value = _state.value.copy(decryptedMessage = plaintext, error = null)
                    }.onFailure { e ->
                        val msg = e.message ?: "Decryption failed"
                        _state.value = _state.value.copy(error = msg)
                        _sideEffect.send(SecureNetworkDemoSideEffect.ShowError(msg))
                    }
            }
        }
    }
