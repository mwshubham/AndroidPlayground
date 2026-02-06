package com.example.android.playground.core.navigation

import kotlinx.serialization.Serializable

/**
 * Constants for navigation parameters
 */
object NavigationConstants {
    const val NEW_NOTE_ID = "new"
}

@Serializable
object FeedRoute

@Serializable
object ImageUploadRoute

@Serializable
object LoginRoute

@Serializable
object NoteListRoute

@Serializable
data class NoteDetailRoute(
    val noteId: String = NavigationConstants.NEW_NOTE_ID
)
