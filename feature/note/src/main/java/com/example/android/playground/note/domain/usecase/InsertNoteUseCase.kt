package com.example.android.playground.note.domain.usecase

import com.example.android.playground.note.domain.model.Note
import com.example.android.playground.note.domain.repository.NoteRepository
import javax.inject.Inject

class InsertNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note): Long {
        return repository.insertNote(note)
    }
}
