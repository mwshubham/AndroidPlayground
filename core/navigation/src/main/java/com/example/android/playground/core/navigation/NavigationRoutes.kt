package com.example.android.playground.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

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
    val noteId: Long? = null
) : NavKey
