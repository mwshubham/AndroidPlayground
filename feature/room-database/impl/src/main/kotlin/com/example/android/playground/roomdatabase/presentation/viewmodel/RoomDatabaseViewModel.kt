package com.example.android.playground.roomdatabase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.roomdatabase.domain.usecase.GetAuthorsWithBooksUseCase
import com.example.android.playground.roomdatabase.domain.usecase.GetBooksWithTagsUseCase
import com.example.android.playground.roomdatabase.domain.usecase.SeedLibraryDataUseCase
import com.example.android.playground.roomdatabase.presentation.intent.RoomDatabaseIntent
import com.example.android.playground.roomdatabase.presentation.mapper.toUiModel
import com.example.android.playground.roomdatabase.presentation.sideeffect.RoomDatabaseSideEffect
import com.example.android.playground.roomdatabase.presentation.state.RoomDatabaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomDatabaseViewModel
    @Inject
    constructor(
        private val getAuthorsWithBooks: GetAuthorsWithBooksUseCase,
        private val getBooksWithTags: GetBooksWithTagsUseCase,
        private val seedLibraryData: SeedLibraryDataUseCase,
    ) : ViewModel() {

        private val _state = MutableStateFlow(RoomDatabaseState())
        val state: StateFlow<RoomDatabaseState> = _state.asStateFlow()

        private val _sideEffect = Channel<RoomDatabaseSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        init {
            handleIntent(RoomDatabaseIntent.LoadData)
        }

        fun handleIntent(intent: RoomDatabaseIntent) {
            when (intent) {
                is RoomDatabaseIntent.LoadData -> loadData()
                is RoomDatabaseIntent.OnTabSelected -> _state.update { it.copy(selectedTab = intent.tab) }
                is RoomDatabaseIntent.NavigateBack -> viewModelScope.launch {
                    _sideEffect.send(RoomDatabaseSideEffect.NavigateBack)
                }
            }
        }

        private fun loadData() {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true, error = null) }
                runCatching { seedLibraryData() }
                    .onFailure { e ->
                        _state.update { it.copy(isLoading = false, error = e.message) }
                        return@launch
                    }

                combine(
                    getAuthorsWithBooks(),
                    getBooksWithTags(),
                ) { authors, books -> authors to books }
                    .collect { (authors, books) ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                authorsWithBooks = authors.map { a -> a.toUiModel() },
                                booksWithTags = books.map { b -> b.toUiModel() },
                            )
                        }
                    }
            }
        }
    }
