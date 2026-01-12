package com.example.android.playground.note.presentation.model

/**
 * UI model for Note with pre-formatted strings to avoid formatting in composables
 */
data class NoteDetailUiModel(
    val id: Long,
    val title: String,
    val content: String,
    val createdAtFormatted: String,
    val updatedAtFormatted: String,
)
