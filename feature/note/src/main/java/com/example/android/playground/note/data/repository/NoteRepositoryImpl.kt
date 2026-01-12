package com.example.android.playground.note.data.repository

import com.example.android.playground.note.data.local.NoteDao
import com.example.android.playground.note.data.local.toDomainModel
import com.example.android.playground.note.data.local.toDomainModelList
import com.example.android.playground.note.data.local.toEntity
import com.example.android.playground.note.domain.model.Note
import com.example.android.playground.note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl
    @Inject
    constructor(
        private val noteDao: NoteDao,
    ) : NoteRepository {
        override fun getAllNotes(): Flow<List<Note>> =
            noteDao.getAllNotes().map { entities ->
                entities.toDomainModelList()
            }

        override suspend fun getNoteById(id: Long): Note? = noteDao.getNoteById(id)?.toDomainModel()

        override suspend fun insertNote(note: Note): Long = noteDao.insertNote(note.toEntity())

        override suspend fun updateNoteContent(
            id: Long,
            title: String,
            content: String,
        ) {
            noteDao.updateNoteContent(
                id = id,
                title = title,
                content = content,
                updatedAt = System.currentTimeMillis(),
            )
        }

        override suspend fun deleteNoteById(id: Long) {
            noteDao.deleteNoteById(id)
        }

        override suspend fun getNoteCount(): Int = noteDao.getNoteCount()
    }
