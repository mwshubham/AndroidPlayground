package com.example.android.playground.annotationprocessing.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.annotationprocessing.presentation.intent.AnnotationProcessingIntent
import com.example.android.playground.annotationprocessing.presentation.sideeffect.AnnotationProcessingSideEffect
import com.example.android.playground.annotationprocessing.presentation.state.AnnotationProcessingState
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
class AnnotationProcessingViewModel
    @Inject
    constructor() : ViewModel() {
        private val _state = MutableStateFlow(AnnotationProcessingState())
        val state: StateFlow<AnnotationProcessingState> = _state.asStateFlow()

        private val _sideEffect = Channel<AnnotationProcessingSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        fun handleIntent(intent: AnnotationProcessingIntent) {
            when (intent) {
                is AnnotationProcessingIntent.SelectTab ->
                    _state.update { it.copy(selectedTab = intent.tab) }

                is AnnotationProcessingIntent.NavigateBack ->
                    viewModelScope.launch {
                        _sideEffect.send(AnnotationProcessingSideEffect.NavigateBack)
                    }
            }
        }
    }
