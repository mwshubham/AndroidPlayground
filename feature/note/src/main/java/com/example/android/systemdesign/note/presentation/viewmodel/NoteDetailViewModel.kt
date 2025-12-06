package com.example.android.systemdesign.note.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.systemdesign.note.domain.model.Note
import com.example.android.systemdesign.note.domain.usecase.GetNoteByIdUseCase
import com.example.android.systemdesign.note.domain.usecase.InsertNoteUseCase
import com.example.android.systemdesign.note.domain.usecase.UpdateNoteUseCase
import com.example.android.systemdesign.note.presentation.state.NoteDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(NoteDetailState())
    val state: StateFlow<NoteDetailState> = _state.asStateFlow()

    private val noteId: Long? = savedStateHandle.get<String>("noteId")?.toLongOrNull()

    init {
        noteId?.let { id ->
            if (id > 0) {
                loadNote(id)
            } else {
                // Create new note mode
                _state.value = _state.value.copy(isEditing = true)
            }
        } ?: run {
            // Create new note mode
            _state.value = _state.value.copy(isEditing = true)
        }
    }

    private fun loadNote(id: Long) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)
                val note = getNoteByIdUseCase(id)
                if (note != null) {
                    _state.value = _state.value.copy(
                        note = note,
                        title = note.title,
                        content = note.content,
                        isLoading = false
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Note not found"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load note"
                )
            }
        }
    }

    fun updateTitle(title: String) {
        _state.value = _state.value.copy(title = title)
    }

    fun updateContent(content: String) {
        _state.value = _state.value.copy(content = content)
    }

    fun toggleEditing() {
        val currentNote = _state.value.note
        if (_state.value.isEditing) {
            // Reset to original values when canceling edit
            _state.value = _state.value.copy(
                isEditing = false,
                title = currentNote?.title ?: "",
                content = currentNote?.content ?: ""
            )
        } else {
            _state.value = _state.value.copy(isEditing = true)
        }
    }

    fun saveNote(): Boolean {
        val title = _state.value.title.trim()
        if (title.isBlank()) {
            _state.value = _state.value.copy(error = "Title cannot be empty")
            return false
        }

        viewModelScope.launch {
            try {
                val currentNote = _state.value.note
                if (currentNote != null) {
                    val updatedNote = currentNote.copy(
                        title = title,
                        content = _state.value.content.trim()
                    )
                    updateNoteUseCase(updatedNote)
                    _state.value = _state.value.copy(
                        note = updatedNote,
                        isEditing = false
                    )
                } else {
                    val newNote = Note(
                        title = title,
                        content = _state.value.content.trim()
                    )
                    val noteId = insertNoteUseCase(newNote)
                    val savedNote = newNote.copy(id = noteId)
                    _state.value = _state.value.copy(
                        note = savedNote,
                        isEditing = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Failed to save note"
                )
            }
        }
        return true
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
