package com.example.android.systemdesign.note.data.repository

import com.example.android.systemdesign.note.data.local.NoteDao
import com.example.android.systemdesign.note.data.local.toDomainModel
import com.example.android.systemdesign.note.data.local.toDomainModelList
import com.example.android.systemdesign.note.data.local.toEntity
import com.example.android.systemdesign.note.domain.model.Note
import com.example.android.systemdesign.note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.toDomainModelList()
        }
    }

    override suspend fun getNoteById(id: Long): Note? {
        return noteDao.getNoteById(id)?.toDomainModel()
    }

    override suspend fun insertNote(note: Note): Long {
        return noteDao.insertNote(note.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.toEntity())
    }

    override suspend fun deleteNoteById(id: Long) {
        noteDao.deleteNoteById(id)
    }

    override suspend fun getNoteCount(): Int {
        return noteDao.getNoteCount()
    }
}
