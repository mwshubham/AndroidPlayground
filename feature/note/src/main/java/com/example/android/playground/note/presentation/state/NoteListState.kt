package com.example.android.playground.note.presentation.state

import com.example.android.playground.note.presentation.model.NoteListItemUiModel

data class NoteListState(
    val notes: List<NoteListItemUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
)
