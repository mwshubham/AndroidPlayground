package com.example.android.playground.note.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.note.domain.model.Note
import com.example.android.playground.note.domain.usecase.GetNoteByIdUseCase
import com.example.android.playground.note.domain.usecase.InsertNoteUseCase
import com.example.android.playground.note.domain.usecase.UpdateNoteUseCase
import com.example.android.playground.note.presentation.intent.NoteDetailIntent
import com.example.android.playground.note.presentation.sideeffect.NoteDetailSideEffect
import com.example.android.playground.note.presentation.state.NoteDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel
    @Inject
    constructor(
        private val getNoteByIdUseCase: GetNoteByIdUseCase,
        private val insertNoteUseCase: InsertNoteUseCase,
        private val updateNoteUseCase: UpdateNoteUseCase,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val _state = MutableStateFlow(NoteDetailState())
        val state: StateFlow<NoteDetailState> = _state.asStateFlow()

        private val _sideEffect = Channel<NoteDetailSideEffect>()
        val sideEffect = _sideEffect.receiveAsFlow()

        private val noteId: Long? = savedStateHandle.get<String>("noteId")?.toLongOrNull()

        init {
            noteId?.let { id ->
                handleIntent(NoteDetailIntent.LoadNote(id))
            } ?: handleIntent(NoteDetailIntent.InitializeNewNote)
        }

        /**
         * Handle user intents in MVI pattern
         */
        fun handleIntent(intent: NoteDetailIntent) {
            when (intent) {
                is NoteDetailIntent.LoadNote -> loadNote(intent.noteId)
                is NoteDetailIntent.InitializeNewNote -> initializeNewNote()
                is NoteDetailIntent.UpdateTitle -> updateTitle(intent.title)
                is NoteDetailIntent.UpdateContent -> updateContent(intent.content)
                is NoteDetailIntent.SaveNote -> saveNote()
                is NoteDetailIntent.EditNote -> editNote()
                is NoteDetailIntent.CancelEditing -> navigateBack()
                is NoteDetailIntent.ClearError -> clearError()
                is NoteDetailIntent.NavigateBack -> navigateBack()
            }
        }

        private fun editNote() {
            _state.value = state.value.copy(isEditing = true)
        }

        private fun loadNote(id: Long) {
            viewModelScope.launch {
                try {
                    _state.value = _state.value.copy(isLoading = true, error = null)
                    val note = getNoteByIdUseCase(id)
                    if (note != null) {
                        _state.value =
                            _state.value.copy(
                                note = note,
                                title = note.title,
                                content = note.content,
                                isLoading = false,
                            )
                    } else {
                        _state.value =
                            _state.value.copy(
                                isLoading = false,
                                error = "Note not found",
                            )
                        _sideEffect.send(NoteDetailSideEffect.ShowErrorMessage("Note not found"))
                    }
                } catch (e: Exception) {
                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load note",
                        )
                    _sideEffect.send(
                        NoteDetailSideEffect.ShowErrorMessage(e.message ?: "Failed to load note"),
                    )
                }
            }
        }

        private fun initializeNewNote() {
            _state.value =
                _state.value.copy(
                    isEditing = true,
                    note = null,
                    title = "",
                    content = "",
                )
        }

        private fun updateTitle(title: String) {
            _state.value = _state.value.copy(title = title)
        }

        private fun updateContent(content: String) {
            _state.value = _state.value.copy(content = content)
        }

        private fun saveNote() {
            val title = _state.value.title.trim()
            if (title.isBlank()) {
                _state.value = _state.value.copy(error = "Title cannot be empty")
                _sideEffect.trySend(NoteDetailSideEffect.ShowErrorMessage("Title cannot be empty"))
                return
            }

            viewModelScope.launch {
                try {
                    val currentNote = _state.value.note
                    if (currentNote != null) {
                        // Update existing note
                        val updatedNote =
                            currentNote.copy(
                                title = title,
                                content = _state.value.content.trim(),
                                updatedAt = System.currentTimeMillis(),
                            )
                        updateNoteUseCase(updatedNote)
                        _state.value =
                            _state.value.copy(
                                note = updatedNote,
                                isEditing = false,
                                error = null,
                            )
                        _sideEffect.send(NoteDetailSideEffect.ShowSuccessMessage("Note updated successfully"))
                    } else {
                        // Create new note
                        val newNote =
                            Note(
                                title = title,
                                content = _state.value.content.trim(),
                                createdAt = System.currentTimeMillis(),
                                updatedAt = System.currentTimeMillis(),
                            )
                        val noteId = insertNoteUseCase(newNote)
                        val savedNote = newNote.copy(id = noteId)
                        _state.value =
                            _state.value.copy(
                                note = savedNote,
                                isEditing = false,
                                error = null,
                            )
                        _sideEffect.send(NoteDetailSideEffect.ShowSuccessMessage("Note created successfully"))
                    }
                } catch (e: Exception) {
                    val errorMessage = e.message ?: "Failed to save note"
                    _state.value = _state.value.copy(error = errorMessage)
                    _sideEffect.send(NoteDetailSideEffect.ShowErrorMessage(errorMessage))
                }
            }
        }

        private fun clearError() {
            _state.value = _state.value.copy(error = null)
        }

        private fun navigateBack() {
            viewModelScope.launch {
                _sideEffect.send(NoteDetailSideEffect.NavigateBack)
            }
        }
    }
