package com.example.android.playground.note.domain.repository

import com.example.android.playground.note.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>

    suspend fun getNoteById(id: Long): Note?

    suspend fun insertNote(note: Note): Long

    suspend fun updateNoteContent(
        id: Long,
        title: String,
        content: String,
    )

    suspend fun deleteNoteById(id: Long)

    suspend fun getNoteCount(): Int
}
