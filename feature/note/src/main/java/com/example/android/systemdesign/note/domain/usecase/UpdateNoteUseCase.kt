package com.example.android.systemdesign.note.domain.usecase

import com.example.android.systemdesign.note.domain.model.Note
import com.example.android.systemdesign.note.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.updateNote(note.copy(updatedAt = System.currentTimeMillis()))
    }
}
