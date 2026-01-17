package com.example.android.playground.note.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.common.AppConstants
import com.example.android.playground.note.domain.usecase.DeleteNoteUseCase
import com.example.android.playground.note.domain.usecase.GetNotesUseCase
import com.example.android.playground.note.presentation.intent.NoteListIntent
import com.example.android.playground.note.presentation.mapper.NoteUiMapper
import com.example.android.playground.note.presentation.sideeffect.NoteListSideEffect
import com.example.android.playground.note.presentation.state.NoteListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class NoteListViewModel
    @Inject
    constructor(
        private val getNotesUseCase: GetNotesUseCase,
        private val deleteNoteUseCase: DeleteNoteUseCase,
    ) : ViewModel() {
        companion object {
            private const val TAG = "NoteListViewModel"
        }

        private val loadTrigger = MutableStateFlow(Unit)
        private val searchQuery = MutableStateFlow("")

        private val _sideEffect = Channel<NoteListSideEffect>()
        val sideEffect = _sideEffect.receiveAsFlow()

        /**
         * Debounced search query for filtering
         */
        private val debouncedSearchQuery = searchQuery.debounce(AppConstants.SEARCH_DEBOUNCE_TIMEOUT)

        /**
         * Notes data flow - fetched only when loadTrigger changes
         */
        private val notesData =
            loadTrigger
                .flatMapLatest {
                    Log.d(TAG, "Loading notes from use case")
                    getNotesUseCase()
                        .map { notes ->
                            Log.d(TAG, "Fetched ${notes.size} notes from use case")
                            // Map to UI models on Default dispatcher
                            withContext(Dispatchers.Default) {
                                notes.map { note ->
                                    NoteUiMapper.toListUiModel(note)
                                }
                            }
                        }.catch { exception ->
                            Log.e(TAG, "Error fetching notes", exception)
                            _sideEffect.send(
                                NoteListSideEffect.ShowErrorMessage("Failed to load notes: ${exception.message}"),
                            )
                            emit(emptyList()) // Emit empty list on error
                        }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(AppConstants.STATEFLOW_SUBSCRIPTION_TIMEOUT),
                    initialValue = emptyList(),
                )

        /**
         * Filtered notes flow - only triggered by debounced search query and notes data changes
         */
        private val filteredNotes =
            combine(
                notesData,
                debouncedSearchQuery,
            ) { notes, query ->
                Log.d(TAG, "Filtering notes with query: '$query'")

                if (query.isNotBlank()) {
                    withContext(Dispatchers.Default) {
                        Log.d(TAG, "Applying filter to ${notes.size} notes")
                        notes.filter { noteUi ->
                            noteUi.title.contains(query, ignoreCase = true) ||
                                noteUi.content.contains(query, ignoreCase = true)
                        }
                    }
                } else {
                    Log.d(TAG, "No filter applied, using all notes")
                    notes
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(AppConstants.STATEFLOW_SUBSCRIPTION_TIMEOUT),
                initialValue = emptyList(),
            )

        /**
         * Combined state flow that only handles UI state composition
         */
        val state: StateFlow<NoteListState> =
            combine(
                filteredNotes,
                searchQuery, // Only immediate search query for UI state
            ) { notes, currentQuery ->
                NoteListState(
                    notes = notes,
                    searchQuery = currentQuery,
                    isLoading = false,
                    error = null,
                )
            }.onStart {
                // Emit initial loading state
                emit(NoteListState(isLoading = true))
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(AppConstants.STATEFLOW_SUBSCRIPTION_TIMEOUT),
                initialValue = NoteListState(isLoading = true),
            )

        /**
         * Handle user intents in MVI pattern
         */
        fun handleIntent(intent: NoteListIntent) {
            when (intent) {
                is NoteListIntent.LoadNotes -> refreshNotes()
                is NoteListIntent.SearchNotes -> searchNotes(intent.query)
                is NoteListIntent.DeleteNote -> deleteNote(intent.noteId)
                is NoteListIntent.ClearError -> clearError()
                is NoteListIntent.NavigateToDetail -> navigateToDetail(intent.noteId)
                is NoteListIntent.NavigateToAdd -> navigateToAdd()
            }
        }

        private fun refreshNotes() {
            // Trigger reload by emitting new value
            loadTrigger.value = Unit
        }

        private fun searchNotes(query: String) {
            searchQuery.value = query
        }

        private fun deleteNote(noteId: Long) {
            viewModelScope.launch {
                try {
                    deleteNoteUseCase(noteId)
                    _sideEffect.send(
                        NoteListSideEffect.ShowSuccessMessage("Note deleted successfully"),
                    )
                    // No need to refresh - getNotesUseCase() Flow will automatically emit updated data
                } catch (exception: Exception) {
                    _sideEffect.send(
                        NoteListSideEffect.ShowErrorMessage("Failed to delete note: ${exception.message}"),
                    )
                }
            }
        }

        private fun clearError() {
            // Error clearing will be handled by the next state emission
            refreshNotes()
        }

        private fun navigateToDetail(noteId: Long) {
            viewModelScope.launch {
                _sideEffect.send(NoteListSideEffect.NavigateToNoteDetail(noteId))
            }
        }

        private fun navigateToAdd() {
            viewModelScope.launch {
                _sideEffect.send(NoteListSideEffect.NavigateToAddNote)
            }
        }
    }
