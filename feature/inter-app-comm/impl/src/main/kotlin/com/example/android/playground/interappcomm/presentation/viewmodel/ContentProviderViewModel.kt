package com.example.android.playground.interappcomm.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.interappcomm.data.store.InterAppMessageStore
import com.example.android.playground.interappcomm.domain.usecase.ReadFromProviderUseCase
import com.example.android.playground.interappcomm.domain.usecase.WriteToProviderUseCase
import com.example.android.playground.interappcomm.presentation.intent.ContentProviderIntent
import com.example.android.playground.interappcomm.presentation.sideeffect.ContentProviderSideEffect
import com.example.android.playground.interappcomm.presentation.state.ContentProviderState
import com.example.android.playground.interappcomm.util.InterAppCommConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ContentProviderViewModel
    @Inject
    constructor(
        @param:ApplicationContext private val context: Context,
        private val writeToProvider: WriteToProviderUseCase,
        private val readFromProvider: ReadFromProviderUseCase,
        private val store: InterAppMessageStore,
    ) : ViewModel() {
        private val _state = MutableStateFlow(ContentProviderState())
        val state: StateFlow<ContentProviderState> = _state.asStateFlow()

        private val _sideEffect = Channel<ContentProviderSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        init {
            _state.update {
                it.copy(
                    currentPackage = context.packageName,
                    targetPackage = InterAppCommConstants.getTargetPackage(context.packageName),
                )
            }
            refreshOwnMessages()
        }

        fun handleIntent(intent: ContentProviderIntent) {
            when (intent) {
                ContentProviderIntent.LoadData -> refreshOwnMessages()
                is ContentProviderIntent.OnInputChanged -> _state.update { it.copy(inputText = intent.text) }
                ContentProviderIntent.WriteToOtherApp -> writeToOther()
                ContentProviderIntent.ReadFromOtherApp -> readFromOther()
                ContentProviderIntent.ClearOwnMessages -> {
                    store.clearContentProviderMessages()
                    refreshOwnMessages()
                }
                ContentProviderIntent.NavigateBack ->
                    viewModelScope.launch {
                        _sideEffect.send(ContentProviderSideEffect.NavigateBack)
                    }
            }
        }

        private fun refreshOwnMessages() {
            _state.update {
                it.copy(ownMessages = store.getContentProviderMessages())
            }
        }

        private fun writeToOther() {
            val content = _state.value.inputText.trim()
            if (content.isEmpty()) return
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true, error = null) }
                val result = withContext(Dispatchers.IO) { writeToProvider(content) }
                result.fold(
                    onSuccess = { _ ->
                        _state.update {
                            it.copy(isLoading = false, inputText = "", error = null)
                        }
                        _sideEffect.send(ContentProviderSideEffect.ShowMessage("Written to other app's provider"))
                    },
                    onFailure = { e ->
                        _state.update { it.copy(isLoading = false, error = e.message) }
                    },
                )
            }
        }

        private fun readFromOther() {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true, error = null) }
                val result = withContext(Dispatchers.IO) { readFromProvider() }
                result.fold(
                    onSuccess = { messages ->
                        _state.update { it.copy(isLoading = false, remoteMessages = messages, error = null) }
                    },
                    onFailure = { e ->
                        _state.update { it.copy(isLoading = false, error = e.message) }
                    },
                )
            }
        }
    }
