package com.example.android.playground.note.presentation.intent

/**
 * Represents user intents/actions for the Note Detail screen in MVI pattern
 */
sealed interface NoteDetailIntent {

    /**
     * Load a specific note by ID
     * @param noteId ID of the note to load
     */
    data class LoadNote(val noteId: Long) : NoteDetailIntent

    /**
     * Initialize screen for creating a new note
     */
    data object InitializeNewNote : NoteDetailIntent

    /**
     * Update the note title
     * @param title New title text
     */
    data class UpdateTitle(val title: String) : NoteDetailIntent

    /**
     * Update the note content
     * @param content New content text
     */
    data class UpdateContent(val content: String) : NoteDetailIntent

    /**
     * Save the current note (create new or update existing)
     */
    data object SaveNote : NoteDetailIntent

    /**
     * Edit the current note
     */
    data object EditNote : NoteDetailIntent

    /**
     * Cancel editing and revert changes
     */
    data object CancelEditing : NoteDetailIntent

    /**
     * Clear any existing error state
     */
    data object ClearError : NoteDetailIntent

    /**
     * Navigate back to previous screen
     */
    data object NavigateBack : NoteDetailIntent
}
