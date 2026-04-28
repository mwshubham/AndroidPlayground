package com.example.android.playground.interappcomm.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.interappcomm.domain.usecase.GetBroadcastMessagesUseCase
import com.example.android.playground.interappcomm.domain.usecase.SendBroadcastUseCase
import com.example.android.playground.interappcomm.data.store.InterAppMessageStore
import com.example.android.playground.interappcomm.presentation.intent.BroadcastIntent
import com.example.android.playground.interappcomm.presentation.sideeffect.BroadcastSideEffect
import com.example.android.playground.interappcomm.presentation.state.BroadcastState
import com.example.android.playground.interappcomm.util.InterAppCommConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BroadcastViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val sendBroadcast: SendBroadcastUseCase,
    getBroadcastMessages: GetBroadcastMessagesUseCase,
    private val store: InterAppMessageStore,
) : ViewModel() {

    private val _state = MutableStateFlow(BroadcastState())
    val state: StateFlow<BroadcastState> = _state.asStateFlow()

    private val _sideEffect = Channel<BroadcastSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        _state.update {
            it.copy(
                currentPackage = context.packageName,
                targetPackage = InterAppCommConstants.getTargetPackage(context.packageName),
            )
        }
        // Observe the shared message store — updates arrive when the BroadcastReceiver fires
        getBroadcastMessages()
            .onEach { messages -> _state.update { it.copy(messages = messages) } }
            .launchIn(viewModelScope)
    }

    fun handleIntent(intent: BroadcastIntent) {
        when (intent) {
            BroadcastIntent.LoadData -> Unit // init handles this
            is BroadcastIntent.OnInputChanged -> _state.update { it.copy(inputText = intent.text) }
            BroadcastIntent.SendBroadcast -> sendMessage()
            BroadcastIntent.ClearMessages -> store.clearBroadcastMessages()
            BroadcastIntent.NavigateBack -> viewModelScope.launch {
                _sideEffect.send(BroadcastSideEffect.NavigateBack)
            }
        }
    }

    private fun sendMessage() {
        val content = _state.value.inputText.trim()
        if (content.isEmpty()) return
        viewModelScope.launch {
            val sentMessage = sendBroadcast(content)
            // Add the SENT message to the store so it appears in the local log
            store.addBroadcastMessage(sentMessage)
            _state.update { it.copy(inputText = "") }
        }
    }
}
