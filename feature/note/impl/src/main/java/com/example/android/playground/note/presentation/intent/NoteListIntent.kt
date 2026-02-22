package com.example.android.playground.note.presentation.intent

/**
 * Represents user intents/actions for the Note List screen in MVI pattern
 */
sealed interface NoteListIntent {
    /**
     * Load all notes from the repository
     */
    data object LoadNotes : NoteListIntent

    /**
     * Search notes based on query
     * @param query Search query string
     */
    data class SearchNotes(
        val query: String,
    ) : NoteListIntent

    /**
     * Delete a specific note
     * @param noteId ID of the note to delete
     */
    data class DeleteNote(
        val noteId: Long,
    ) : NoteListIntent

    /**
     * Clear any existing error state
     */
    data object ClearError : NoteListIntent

    /**
     * Navigate to note detail screen
     * @param noteId ID of the note to view/edit
     */
    data class NavigateToDetail(
        val noteId: Long,
    ) : NoteListIntent

    /**
     * Navigate to add new note screen
     */
    data object NavigateToAdd : NoteListIntent
}
