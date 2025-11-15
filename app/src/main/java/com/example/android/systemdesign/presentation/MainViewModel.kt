package com.example.android.systemdesign.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.systemdesign.domain.usecase.GetSystemDesignTopicsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getSystemDesignTopicsUseCase: GetSystemDesignTopicsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        handleIntent(MainIntent.LoadTopics)
    }

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.LoadTopics -> loadTopics()
        }
    }

    private fun loadTopics() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val topics = getSystemDesignTopicsUseCase()
                _state.value = _state.value.copy(
                    topics = topics,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }
}
