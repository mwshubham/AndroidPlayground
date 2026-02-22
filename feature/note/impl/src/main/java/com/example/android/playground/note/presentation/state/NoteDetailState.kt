package com.example.android.playground.note.presentation.state

import com.example.android.playground.note.presentation.model.NoteDetailUiModel

data class NoteDetailState(
    val note: NoteDetailUiModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditing: Boolean = false,
    val title: String = "",
    val content: String = "",
)
