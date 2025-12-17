package com.example.android.playground.note.presentation.state

import com.example.android.playground.note.domain.model.Note

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)
