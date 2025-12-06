package com.example.android.systemdesign.note.presentation.sideeffect

/**
 * Represents side effects for the Note List screen in MVI pattern
 * Side effects are one-time events that don't belong to the state
 */
sealed interface NoteListSideEffect {

    /**
     * Navigate to note detail screen
     * @param noteId ID of the note to view/edit
     */
    data class NavigateToNoteDetail(val noteId: Long) : NoteListSideEffect

    /**
     * Navigate to add new note screen
     */
    data object NavigateToAddNote : NoteListSideEffect

    /**
     * Navigate back to previous screen
     */
    data object NavigateBack : NoteListSideEffect

    /**
     * Show success message when note is deleted
     * @param message Success message to display
     */
    data class ShowSuccessMessage(val message: String) : NoteListSideEffect

    /**
     * Show error message
     * @param message Error message to display
     */
    data class ShowErrorMessage(val message: String) : NoteListSideEffect
}
