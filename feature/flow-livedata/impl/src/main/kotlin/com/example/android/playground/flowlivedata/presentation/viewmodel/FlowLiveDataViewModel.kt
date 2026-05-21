package com.example.android.playground.flowlivedata.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.flowlivedata.presentation.intent.FlowLiveDataIntent
import com.example.android.playground.flowlivedata.presentation.model.StreamType
import com.example.android.playground.flowlivedata.presentation.sideeffect.FlowLiveDataSideEffect
import com.example.android.playground.flowlivedata.presentation.state.FlowLiveDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class FlowLiveDataViewModel
    @Inject
    constructor() : ViewModel() {
        private val _state = MutableStateFlow(FlowLiveDataState())
        val state: StateFlow<FlowLiveDataState> = _state.asStateFlow()

        private val _sideEffect = Channel<FlowLiveDataSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        // Reactive primitives used for the live demo
        private val demoStateFlow = MutableStateFlow(0)
        private val demoSharedFlow = MutableSharedFlow<Int>(replay = 0)
        private val demoLiveData = MutableLiveData(0)
        private val demoChannel = Channel<Int>(Channel.BUFFERED)

        private var emitCounter = 0
        private var emitJob: Job? = null
        private var collectorBJob: Job? = null
        private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        init {
            Timber.d("ViewModel initialised")
            // Mirror LiveData values into state via observeForever
            demoLiveData.observeForever { value ->
                Timber.d("LiveData → value=$value")
                _state.update { it.copy(liveDataValue = value) }
            }
            // Collect StateFlow and mirror into UI state
            viewModelScope.launch {
                demoStateFlow.collect { value ->
                    Timber.d("StateFlow → value=$value")
                    _state.update { it.copy(stateFlowValue = value) }
                }
            }
            // Collector A — always active; receives every emission (SharedFlow fans out to all collectors)
            viewModelScope.launch {
                demoSharedFlow.collect { value ->
                    Timber.d("SharedFlow Collector A → value=$value")
                    val entry = "Collector A: #$value at ${LocalTime.now().format(timeFormatter)}"
                    _state.update { it.copy(collectorALog = (it.collectorALog + entry).takeLast(MAX_LOG_ENTRIES)) }
                }
            }
            // Channel consumer — each item received exactly once; no other consumer sees this value
            viewModelScope.launch {
                for (value in demoChannel) {
                    Timber.d("Channel consumed value=$value")
                    val entry = "#$value at ${LocalTime.now().format(timeFormatter)}"
                    _state.update {
                        it.copy(
                            channelLog = (it.channelLog + entry).takeLast(MAX_LOG_ENTRIES),
                            channelPendingCount = (it.channelPendingCount - 1).coerceAtLeast(0),
                        )
                    }
                }
            }
        }

        fun handleIntent(intent: FlowLiveDataIntent) {
            Timber.d("handleIntent: $intent")
            when (intent) {
                is FlowLiveDataIntent.SelectTab -> _state.update { it.copy(selectedTab = intent.tab) }
                is FlowLiveDataIntent.ToggleEmitting -> toggleEmitting()
                is FlowLiveDataIntent.ClearLog -> clearLog()
                is FlowLiveDataIntent.SimulateLateSubscriber -> simulateLateSubscriber()
                is FlowLiveDataIntent.ToggleCollectorB -> toggleCollectorB()
                is FlowLiveDataIntent.NavigateBack -> sendEffect(FlowLiveDataSideEffect.NavigateBack)
            }
        }

        private fun toggleEmitting() {
            if (_state.value.isEmitting) {
                Timber.d("Emitting stopped at counter=$emitCounter")
                emitJob?.cancel()
                emitJob = null
                _state.update { it.copy(isEmitting = false) }
            } else {
                Timber.d("Emitting started")
                _state.update { it.copy(isEmitting = true) }
                emitJob =
                    viewModelScope.launch {
                        while (true) {
                            emitCounter++
                            Timber.v("Emitting #$emitCounter to all streams")
                            demoStateFlow.value = emitCounter
                            demoSharedFlow.emit(emitCounter)
                            demoLiveData.postValue(emitCounter)
                            // Increment pending count; the consumer coroutine decrements it on receive
                            _state.update { it.copy(channelPendingCount = it.channelPendingCount + 1) }
                            demoChannel.trySend(emitCounter)
                            delay(EMIT_INTERVAL_MS)
                        }
                    }
            }
        }

        private fun clearLog() {
            Timber.d("clearLog: tab=${_state.value.selectedTab}")
            when (_state.value.selectedTab) {
                StreamType.SHARED_FLOW -> _state.update { it.copy(collectorALog = emptyList(), collectorBLog = emptyList()) }
                StreamType.CHANNEL -> _state.update { it.copy(channelLog = emptyList()) }
                else -> Unit
            }
        }

        /**
         * Demonstrates StateFlow's replay guarantee: a brand-new collector always
         * receives the current value immediately — no missed emissions.
         */
        private fun simulateLateSubscriber() {
            _state.update { it.copy(lateSubscriberResult = "Subscribing…") }
            viewModelScope.launch {
                val value = demoStateFlow.value
                val result = "Late subscriber got: $value at ${LocalTime.now().format(timeFormatter)}"
                Timber.d("SimulateLateSubscriber: $result")
                _state.update { it.copy(lateSubscriberResult = result) }
            }
        }

        /**
         * Starts or stops Collector B on [demoSharedFlow].
         * While stopped, emitted values are permanently lost —
         * SharedFlow(replay=0) does not buffer values for absent collectors.
         */
        private fun toggleCollectorB() {
            if (_state.value.isCollectorBActive) {
                Timber.d("Collector B stopped — emissions will now be missed")
                collectorBJob?.cancel()
                collectorBJob = null
                _state.update { it.copy(isCollectorBActive = false) }
            } else {
                Timber.d("Collector B started")
                _state.update { it.copy(isCollectorBActive = true) }
                collectorBJob =
                    viewModelScope.launch {
                        demoSharedFlow.collect { value ->
                            Timber.d("SharedFlow Collector B → value=$value")
                            val entry = "Collector B: #$value at ${LocalTime.now().format(timeFormatter)}"
                            _state.update {
                                it.copy(collectorBLog = (it.collectorBLog + entry).takeLast(MAX_LOG_ENTRIES))
                            }
                        }
                    }
            }
        }

        private fun sendEffect(effect: FlowLiveDataSideEffect) {
            Timber.d("sendEffect: $effect")
            viewModelScope.launch { _sideEffect.send(effect) }
        }

        override fun onCleared() {
            Timber.d("onCleared")
            super.onCleared()
            demoChannel.close()
        }

        private companion object {
            const val EMIT_INTERVAL_MS = 1_000L
            const val MAX_LOG_ENTRIES = 50
        }
    }
