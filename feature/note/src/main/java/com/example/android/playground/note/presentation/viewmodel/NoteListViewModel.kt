package com.example.android.playground.note.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.note.domain.usecase.DeleteNoteUseCase
import com.example.android.playground.note.domain.usecase.GetNotesUseCase
import com.example.android.playground.note.presentation.intent.NoteListIntent
import com.example.android.playground.note.presentation.mapper.NoteUiMapper
import com.example.android.playground.note.presentation.sideeffect.NoteListSideEffect
import com.example.android.playground.note.presentation.state.NoteListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel
    @Inject
    constructor(
        private val getNotesUseCase: GetNotesUseCase,
        private val deleteNoteUseCase: DeleteNoteUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(NoteListState())
        val state: StateFlow<NoteListState> = _state.asStateFlow()

        private val _sideEffect = Channel<NoteListSideEffect>()
        val sideEffect = _sideEffect.receiveAsFlow()

        init {
            handleIntent(NoteListIntent.LoadNotes)
        }

        /**
         * Handle user intents in MVI pattern
         */
        fun handleIntent(intent: NoteListIntent) {
            when (intent) {
                is NoteListIntent.LoadNotes -> loadNotes()
                is NoteListIntent.SearchNotes -> searchNotes(intent.query)
                is NoteListIntent.DeleteNote -> deleteNote(intent.noteId)
                is NoteListIntent.ClearError -> clearError()
                is NoteListIntent.NavigateToDetail -> navigateToDetail(intent.noteId)
                is NoteListIntent.NavigateToAdd -> navigateToAdd()
            }
        }

        private fun loadNotes() {
            viewModelScope.launch {
                _state.value = _state.value.copy(isLoading = true)
                getNotesUseCase()
                    .catch { exception ->
                        _state.value =
                            _state.value.copy(
                                isLoading = false,
                                error = "Failed to load notes: ${exception.message}",
                            )
                        _sideEffect.send(
                            NoteListSideEffect.ShowErrorMessage("Failed to load notes: ${exception.message}"),
                        )
                    }.collect { notes ->
                        val noteUiModels = notes.map { note ->
                            NoteUiMapper.toListUiModel(note)
                        }
                        _state.value =
                            _state.value.copy(
                                notes = noteUiModels,
                                isLoading = false,
                                error = null,
                            )
                    }
            }
        }

        private fun searchNotes(query: String) {
            _state.value = _state.value.copy(searchQuery = query)
        }

        private fun deleteNote(noteId: Long) {
            viewModelScope.launch {
                try {
                    deleteNoteUseCase(noteId)
                    _sideEffect.send(
                        NoteListSideEffect.ShowSuccessMessage("Note deleted successfully"),
                    )
                } catch (exception: Exception) {
                    _state.value =
                        _state.value.copy(
                            error = "Failed to delete note: ${exception.message}",
                        )
                    _sideEffect.send(
                        NoteListSideEffect.ShowErrorMessage("Failed to delete note: ${exception.message}"),
                    )
                }
            }
        }

        private fun clearError() {
            _state.value = _state.value.copy(error = null)
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
