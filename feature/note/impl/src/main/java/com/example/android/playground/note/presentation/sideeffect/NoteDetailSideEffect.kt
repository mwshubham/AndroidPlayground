package com.example.android.playground.note.presentation.sideeffect

/**
 * Represents side effects for the Note Detail screen in MVI pattern
 * Side effects are one-time events that don't belong to the state
 */
sealed interface NoteDetailSideEffect {
    /**
     * Navigate back to the previous screen
     */
    data object NavigateBack : NoteDetailSideEffect

    /**
     * Show success message when note is saved
     * @param message Success message to display
     */
    data class ShowSuccessMessage(
        val message: String,
    ) : NoteDetailSideEffect

    /**
     * Show error message
     * @param message Error message to display
     */
    data class ShowErrorMessage(
        val message: String,
    ) : NoteDetailSideEffect
}
