package com.example.android.playground.note.presentation.state

import com.example.android.playground.note.presentation.model.NoteUiModel

data class NoteDetailState(
    val note: NoteUiModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditing: Boolean = false,
    val title: String = "",
    val content: String = "",
)
