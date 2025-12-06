package com.example.android.systemdesign.note.presentation.state

import com.example.android.systemdesign.note.domain.model.Note

data class NoteDetailState(
    val note: Note? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditing: Boolean = false,
    val title: String = "",
    val content: String = ""
)