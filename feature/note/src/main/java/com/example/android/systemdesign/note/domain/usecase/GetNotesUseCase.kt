package com.example.android.systemdesign.note.domain.usecase

import com.example.android.systemdesign.note.domain.model.Note
import com.example.android.systemdesign.note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getAllNotes()
    }
}
