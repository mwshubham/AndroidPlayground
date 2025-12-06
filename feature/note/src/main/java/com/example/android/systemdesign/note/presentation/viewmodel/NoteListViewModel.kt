package com.example.android.systemdesign.note.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.systemdesign.note.domain.model.Note
import com.example.android.systemdesign.note.domain.repository.NoteRepository
import com.example.android.systemdesign.note.presentation.state.NoteListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NoteListState())
    val state: StateFlow<NoteListState> = _state.asStateFlow()

    val filteredNotes: StateFlow<List<Note>> = combine(
        flow = noteRepository.getAllNotes(),
        flow2 = _state
    ) { notes, state ->
        val searchQuery = state.searchQuery
        if (searchQuery.isBlank()) {
            notes
        } else {
            notes.filter { note ->
                note.title.contains(searchQuery, ignoreCase = true) ||
                note.content.contains(searchQuery, ignoreCase = true)
            }
        }
    }
        .catch { exception ->
            _state.value = _state.value.copy(
                isLoading = false,
                error = "Failed to load notes: ${exception.message}"
            )
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    init {
        // Initialize loading state - actual data will come through filteredNotes flow
        observeNotes()
    }

    private fun observeNotes() {
        viewModelScope.launch {
            noteRepository.getAllNotes()
                .catch { exception ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Failed to load notes: ${exception.message}"
                    )
                }
                .collect { notes ->
                    _state.value = _state.value.copy(
                        notes = notes,
                        isLoading = false,
                        error = null
                    )
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
    }


    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                noteRepository.deleteNote(note)
                clearError()
            } catch (exception: Exception) {
                _state.value = _state.value.copy(
                    error = "Failed to delete note: ${exception.message}"
                )
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun refresh() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        // No need to call observeNotes() again as it's already running from init
        // The flow will automatically emit new data when the repository changes
    }
}
