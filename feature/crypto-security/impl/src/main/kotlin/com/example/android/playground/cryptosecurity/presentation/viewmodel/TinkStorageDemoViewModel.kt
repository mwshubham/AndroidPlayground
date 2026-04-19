package com.example.android.playground.cryptosecurity.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.cryptosecurity.domain.repository.CryptoSecurityRepository
import com.example.android.playground.cryptosecurity.domain.usecase.LoadTinkDecryptedUseCase
import com.example.android.playground.cryptosecurity.domain.usecase.SaveTinkEncryptedUseCase
import com.example.android.playground.cryptosecurity.presentation.intent.StorageDemoIntent
import com.example.android.playground.cryptosecurity.presentation.sideeffect.StorageDemoSideEffect
import com.example.android.playground.cryptosecurity.presentation.state.StorageDemoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TinkStorageDemoViewModel @Inject constructor(
    private val saveTinkEncrypted: SaveTinkEncryptedUseCase,
    private val loadTinkDecrypted: LoadTinkDecryptedUseCase,
    private val repository: CryptoSecurityRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(StorageDemoState())
    val state: StateFlow<StorageDemoState> = _state.asStateFlow()

    private val _sideEffect = Channel<StorageDemoSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    fun handleIntent(intent: StorageDemoIntent) {
        when (intent) {
            is StorageDemoIntent.KeyChanged -> _state.value = _state.value.copy(inputKey = intent.key)
            is StorageDemoIntent.ValueChanged -> _state.value = _state.value.copy(inputValue = intent.value)
            StorageDemoIntent.EncryptSave -> encryptAndSave()
            StorageDemoIntent.LoadDecrypt -> loadAndDecrypt()
            StorageDemoIntent.Clear -> clear()
            StorageDemoIntent.NavigateBack -> viewModelScope.launch { _sideEffect.send(StorageDemoSideEffect.NavigateBack) }
        }
    }

    private fun encryptAndSave() {
        val key = _state.value.inputKey.trim()
        val value = _state.value.inputValue.trim()
        if (key.isBlank() || value.isBlank()) return
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true, error = null)
            runCatching { saveTinkEncrypted(key, value) }
                .onSuccess {
                    observeRawCiphertext(key)
                    _sideEffect.send(StorageDemoSideEffect.SavedSuccessfully)
                    _state.value = _state.value.copy(isSaving = false, loadedValue = null)
                }
                .onFailure { e ->
                    val msg = e.message ?: "Save failed"
                    _state.value = _state.value.copy(isSaving = false, error = msg)
                    _sideEffect.send(StorageDemoSideEffect.ShowError(msg))
                }
        }
    }

    private fun loadAndDecrypt() {
        val key = _state.value.inputKey.trim()
        if (key.isBlank()) return
        _state.value = _state.value.copy(isLoading = true, error = null)
        loadTinkDecrypted(key)
            .onEach { value ->
                _state.value = _state.value.copy(isLoading = false, loadedValue = value)
            }
            .catch { e ->
                val msg = e.message ?: "Load failed"
                _state.value = _state.value.copy(isLoading = false, error = msg)
                _sideEffect.send(StorageDemoSideEffect.ShowError(msg))
            }
            .launchIn(viewModelScope)
    }

    private fun observeRawCiphertext(key: String) {
        repository.loadTinkRawCiphertext(key)
            .onEach { ct -> _state.value = _state.value.copy(savedCiphertextHex = ct?.toHex()) }
            .catch { /* swallow — display only */ }
            .launchIn(viewModelScope)
    }

    private fun clear() {
        val key = _state.value.inputKey.trim()
        viewModelScope.launch {
            runCatching { repository.clearTinkEntry(key) }
            _state.value = _state.value.copy(
                savedIvHex = null,
                savedCiphertextHex = null,
                loadedValue = null,
                error = null,
            )
        }
    }
}

private fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }
