package com.example.android.systemdesign.note.domain.usecase

import com.example.android.systemdesign.note.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteNoteById(id)
    }
}

