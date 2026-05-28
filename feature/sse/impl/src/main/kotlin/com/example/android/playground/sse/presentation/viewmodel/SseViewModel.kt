package com.example.android.playground.sse.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.sse.domain.model.SseEvent
import com.example.android.playground.sse.domain.usecase.ObserveWikipediaChangesUseCase
import com.example.android.playground.sse.presentation.intent.SseIntent
import com.example.android.playground.sse.presentation.sideeffect.SseSideEffect
import com.example.android.playground.sse.presentation.state.SseConnectionStatus
import com.example.android.playground.sse.presentation.state.SseState
import com.example.android.playground.sse.presentation.state.WikipediaChangeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

private const val MAX_COMMENT_LENGTH = 120
private const val MAX_CHANGES = 50

@HiltViewModel
class SseViewModel
    @Inject
    constructor(
        private val observeWikipediaChangesUseCase: ObserveWikipediaChangesUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(SseState())
        val state: StateFlow<SseState> = _state.asStateFlow()

        private val _sideEffects = Channel<SseSideEffect>()
        val sideEffects = _sideEffects.receiveAsFlow()

        private var subscriptionJob: Job? = null
        private val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        fun processIntent(intent: SseIntent) {
            when (intent) {
                SseIntent.Subscribe -> subscribe()
                SseIntent.Unsubscribe -> unsubscribe()
                SseIntent.Clear -> _state.update { it.copy(changes = emptyList()) }
            }
        }

        private fun subscribe() {
            subscriptionJob?.cancel()
            subscriptionJob =
                viewModelScope.launch {
                    observeWikipediaChangesUseCase().collect { event ->
                        handleEvent(event)
                    }
                }
        }

        private fun unsubscribe() {
            subscriptionJob?.cancel()
            subscriptionJob = null
            _state.update { it.copy(connectionStatus = SseConnectionStatus.Disconnected) }
        }

        private fun handleEvent(event: SseEvent) {
            when (event) {
                SseEvent.Connecting ->
                    _state.update {
                        it.copy(connectionStatus = SseConnectionStatus.Connecting)
                    }

                SseEvent.Connected ->
                    _state.update {
                        it.copy(connectionStatus = SseConnectionStatus.Connected)
                    }

                is SseEvent.Change -> {
                    val change = event.change
                    val uiModel =
                        WikipediaChangeUiModel(
                            title = change.title,
                            user = change.user,
                            wiki = change.wiki,
                            type = change.type,
                            comment =
                                change.comment.take(MAX_COMMENT_LENGTH).let {
                                    if (change.comment.length > MAX_COMMENT_LENGTH) "$it\u2026" else it
                                },
                            formattedTime = timeFormatter.format(Date(change.timestampMs)),
                        )
                    _state.update { state ->
                        state.copy(
                            changes = (listOf(uiModel) + state.changes).take(MAX_CHANGES),
                        )
                    }
                }

                SseEvent.Disconnected ->
                    _state.update {
                        it.copy(connectionStatus = SseConnectionStatus.Disconnected)
                    }

                is SseEvent.Error -> {
                    _state.update { it.copy(connectionStatus = SseConnectionStatus.Error) }
                    _sideEffects.trySend(SseSideEffect.ShowError(event.message))
                }
            }
        }

        override fun onCleared() {
            super.onCleared()
            subscriptionJob?.cancel()
        }
    }
