package com.example.android.playground.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Constants for navigation parameters
 */
object NavigationConstants {
    const val NEW_NOTE_ID = "new"
}

@Serializable
data object FeedRoute : NavKey

@Serializable
data object ImageUploadRoute : NavKey

@Serializable
data object LoginRoute : NavKey

@Serializable
data object NoteListRoute : NavKey

@Serializable
data class NoteDetailRoute(
    val noteId: String = NavigationConstants.NEW_NOTE_ID
) : NavKey
