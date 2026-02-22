package com.example.android.playground.note.presentation.model

/**
 * UI model for Note list items with pre-formatted strings to avoid formatting in composables
 */
data class NoteListItemUiModel(
    val id: Long,
    val title: String,
    val content: String,
    val updatedAtFormatted: String,
)
