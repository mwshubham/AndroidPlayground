package com.example.android.playground.cryptosecurity.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.cryptosecurity.domain.usecase.EncryptForServerUseCase
import com.example.android.playground.cryptosecurity.domain.usecase.SimulateServerDecryptUseCase
import com.example.android.playground.cryptosecurity.presentation.intent.SendEncryptedDemoIntent
import com.example.android.playground.cryptosecurity.presentation.sideeffect.SendEncryptedDemoSideEffect
import com.example.android.playground.cryptosecurity.presentation.state.SendEncryptedDemoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendEncryptedDemoViewModel @Inject constructor(
    private val encryptForServer: EncryptForServerUseCase,
    private val simulateServerDecrypt: SimulateServerDecryptUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SendEncryptedDemoState())
    val state: StateFlow<SendEncryptedDemoState> = _state.asStateFlow()

    private val _sideEffect = Channel<SendEncryptedDemoSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    fun handleIntent(intent: SendEncryptedDemoIntent) {
        when (intent) {
            is SendEncryptedDemoIntent.MessageChanged ->
                _state.value = _state.value.copy(inputMessage = intent.message)
            SendEncryptedDemoIntent.EncryptAndSend -> encrypt()
            SendEncryptedDemoIntent.SimulateServerDecrypt -> decrypt()
            SendEncryptedDemoIntent.Clear ->
                _state.value = SendEncryptedDemoState()
            SendEncryptedDemoIntent.NavigateBack -> viewModelScope.launch { _sideEffect.send(SendEncryptedDemoSideEffect.NavigateBack) }
        }
    }

    private fun encrypt() {
        val message = _state.value.inputMessage.trim()
        if (message.isBlank()) return
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isEncrypting = true,
                error = null,
                encryptedPayload = null,
                serverDecryptedMessage = null,
            )
            runCatching { encryptForServer(message) }
                .onSuccess { payload ->
                    _state.value = _state.value.copy(isEncrypting = false, encryptedPayload = payload)
                }
                .onFailure { e ->
                    val msg = e.message ?: "Encryption failed"
                    _state.value = _state.value.copy(isEncrypting = false, error = msg)
                    _sideEffect.send(SendEncryptedDemoSideEffect.ShowError(msg))
                }
        }
    }

    private fun decrypt() {
        val payload = _state.value.encryptedPayload ?: return
        viewModelScope.launch {
            runCatching { simulateServerDecrypt(payload) }
                .onSuccess { plaintext ->
                    _state.value = _state.value.copy(serverDecryptedMessage = plaintext, error = null)
                }
                .onFailure { e ->
                    val msg = e.message ?: "Server decryption failed"
                    _state.value = _state.value.copy(error = msg)
                    _sideEffect.send(SendEncryptedDemoSideEffect.ShowError(msg))
                }
        }
    }
}
