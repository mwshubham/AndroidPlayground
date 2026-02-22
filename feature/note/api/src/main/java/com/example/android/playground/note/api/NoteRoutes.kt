package com.example.android.playground.note.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object NoteListRoute : NavKey

@Serializable
data class NoteDetailRoute(
    val noteId: Long? = null
) : NavKey
