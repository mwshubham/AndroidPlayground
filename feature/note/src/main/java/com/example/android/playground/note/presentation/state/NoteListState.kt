package com.example.android.playground.note.presentation.state

import com.example.android.playground.note.presentation.model.NoteListUiModel

data class NoteListState(
    val notes: List<NoteListUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
)
