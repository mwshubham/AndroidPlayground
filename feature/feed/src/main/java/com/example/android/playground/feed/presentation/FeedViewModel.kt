package com.example.android.playground.feed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.feed.domain.usecase.GetFeedTopicsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getFeedTopicsUseCase: GetFeedTopicsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FeedState())
    val state: StateFlow<FeedState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<FeedSideEffect>()
    val sideEffect: SharedFlow<FeedSideEffect> = _sideEffect.asSharedFlow()

    init {
        handleIntent(FeedIntent.LoadTopics)
    }

    fun handleIntent(intent: FeedIntent) {
        when (intent) {
            is FeedIntent.LoadTopics -> loadTopics()
            is FeedIntent.RefreshTopics -> refreshTopics()
        }
    }

    private fun loadTopics() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val topics = getFeedTopicsUseCase()
                _state.value = _state.value.copy(
                    topics = topics,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Unknown error occurred"
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = errorMessage
                )
                _sideEffect.emit(FeedSideEffect.ShowError(errorMessage))
            }
        }
    }

    private fun refreshTopics() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true)
            try {
                val topics = getFeedTopicsUseCase()
                _state.value = _state.value.copy(
                    topics = topics,
                    error = null,
                    isRefreshing = false
                )
                _sideEffect.emit(FeedSideEffect.TopicsRefreshed)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Failed to refresh topics"
                _state.value = _state.value.copy(isRefreshing = false)
                _sideEffect.emit(FeedSideEffect.ShowError(errorMessage))
            }
        }
    }
}
