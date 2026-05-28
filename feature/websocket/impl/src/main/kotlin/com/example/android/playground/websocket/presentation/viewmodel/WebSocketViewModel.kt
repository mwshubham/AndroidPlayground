package com.example.android.playground.websocket.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.websocket.domain.model.WebSocketEvent
import com.example.android.playground.websocket.domain.usecase.ObserveBtcTickerUseCase
import com.example.android.playground.websocket.presentation.intent.WebSocketIntent
import com.example.android.playground.websocket.presentation.sideeffect.WebSocketSideEffect
import com.example.android.playground.websocket.presentation.state.BtcTickerUiModel
import com.example.android.playground.websocket.presentation.state.WebSocketConnectionStatus
import com.example.android.playground.websocket.presentation.state.WebSocketState
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

private const val MAX_RECENT_TICKS = 30

@HiltViewModel
class WebSocketViewModel
    @Inject
    constructor(
        private val observeBtcTickerUseCase: ObserveBtcTickerUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(WebSocketState())
        val state: StateFlow<WebSocketState> = _state.asStateFlow()

        private val _sideEffects = Channel<WebSocketSideEffect>()
        val sideEffects = _sideEffects.receiveAsFlow()

        private var tickerJob: Job? = null
        private val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        fun processIntent(intent: WebSocketIntent) {
            when (intent) {
                WebSocketIntent.Connect -> connect()
                WebSocketIntent.Disconnect -> disconnect()
            }
        }

        private fun connect() {
            tickerJob?.cancel()
            tickerJob =
                viewModelScope.launch {
                    observeBtcTickerUseCase().collect { event ->
                        handleEvent(event)
                    }
                }
        }

        private fun disconnect() {
            tickerJob?.cancel()
            tickerJob = null
            _state.update { it.copy(connectionStatus = WebSocketConnectionStatus.Disconnected) }
        }

        private fun handleEvent(event: WebSocketEvent) {
            when (event) {
                WebSocketEvent.Connecting ->
                    _state.update {
                        it.copy(connectionStatus = WebSocketConnectionStatus.Connecting)
                    }

                WebSocketEvent.Connected ->
                    _state.update {
                        it.copy(connectionStatus = WebSocketConnectionStatus.Connected)
                    }

                is WebSocketEvent.Tick -> {
                    val ticker = event.ticker
                    val isPositive = !ticker.priceChange.startsWith("-")
                    val uiModel =
                        BtcTickerUiModel(
                            price = "$${"%.2f".format(ticker.lastPrice.toDoubleOrNull() ?: 0.0)}",
                            priceChange =
                                "%.2f".format(ticker.priceChange.toDoubleOrNull() ?: 0.0),
                            priceChangePercent =
                                "${"%.2f".format(ticker.priceChangePercent.toDoubleOrNull() ?: 0.0)}%",
                            isPositive = isPositive,
                            formattedTime = timeFormatter.format(Date(ticker.eventTimeMs)),
                        )
                    _state.update { state ->
                        state.copy(
                            price = uiModel.price,
                            priceChange = uiModel.priceChange,
                            priceChangePercent = uiModel.priceChangePercent,
                            isPositive = isPositive,
                            highPrice =
                                "$${"%.2f".format(ticker.highPrice.toDoubleOrNull() ?: 0.0)}",
                            lowPrice =
                                "$${"%.2f".format(ticker.lowPrice.toDoubleOrNull() ?: 0.0)}",
                            recentTicks = (listOf(uiModel) + state.recentTicks).take(MAX_RECENT_TICKS),
                        )
                    }
                }

                is WebSocketEvent.Error -> {
                    _state.update { it.copy(connectionStatus = WebSocketConnectionStatus.Error) }
                    _sideEffects.trySend(WebSocketSideEffect.ShowError(event.message))
                }

                is WebSocketEvent.Disconnected ->
                    _state.update {
                        it.copy(connectionStatus = WebSocketConnectionStatus.Disconnected)
                    }
            }
        }

        override fun onCleared() {
            super.onCleared()
            tickerJob?.cancel()
        }
    }
