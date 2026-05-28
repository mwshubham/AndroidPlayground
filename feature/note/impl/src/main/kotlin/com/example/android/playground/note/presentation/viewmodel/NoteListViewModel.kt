package com.example.android.playground.note.presentation.viewmodel

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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class NoteListViewModel
    @Inject
    constructor(
        private val getNotesUseCase: GetNotesUseCase,
        private val deleteNoteUseCase: DeleteNoteUseCase,
    ) : ViewModel() {
        private val loadTrigger = MutableStateFlow(Unit)
        private val searchQuery = MutableStateFlow("")

        private val _sideEffect = Channel<NoteListSideEffect>(Channel.BUFFERED)
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
                    Timber.d("Loading notes from use case")
                    getNotesUseCase()
                        .map { notes ->
                            Timber.d("Fetched ${notes.size} notes from use case")
                            // Map to UI models on Default dispatcher
                            withContext(Dispatchers.Default) {
                                notes.map { note ->
                                    NoteUiMapper.toListUiModel(note)
                                }
                            }
                        }.catch { exception ->
                            Timber.e(exception, "Error fetching notes")
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
                Timber.d("Filtering notes with query: '$query'")

                if (query.isNotBlank()) {
                    withContext(Dispatchers.Default) {
                        Timber.d("Applying filter to ${notes.size} notes")
                        notes.filter { noteUi ->
                            noteUi.title.contains(query, ignoreCase = true) ||
                                noteUi.content.contains(query, ignoreCase = true)
                        }
                    }
                } else {
                    Timber.d("No filter applied, using all notes")
                    notes
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(AppConstants.STATEFLOW_SUBSCRIPTION_TIMEOUT),
                initialValue = emptyList(),
            )

        private val _state = MutableStateFlow(NoteListState(isLoading = true))

        /**
         * Combined state flow that only handles UI state composition
         */
        val state: StateFlow<NoteListState> = _state.asStateFlow()

        init {
            viewModelScope.launch {
                combine(
                    filteredNotes,
                    searchQuery,
                ) { notes, currentQuery ->
                    NoteListState(
                        notes = notes,
                        searchQuery = currentQuery,
                        isLoading = false,
                    )
                }.collect { newState -> _state.update { newState } }
            }
        }

        /**
         * Handle user intents in MVI pattern
         */
        fun handleIntent(intent: NoteListIntent) {
            when (intent) {
                is NoteListIntent.LoadNotes -> refreshNotes()
                is NoteListIntent.SearchNotes -> searchNotes(intent.query)
                is NoteListIntent.DeleteNote -> deleteNote(intent.noteId)
                is NoteListIntent.NavigateToDetail -> navigateToDetail(intent.noteId)
                is NoteListIntent.NavigateToAdd -> navigateToAdd()
                is NoteListIntent.NavigateBack -> navigateBack()
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

        private fun navigateBack() {
            viewModelScope.launch {
                _sideEffect.send(NoteListSideEffect.NavigateBack)
            }
        }
    }
