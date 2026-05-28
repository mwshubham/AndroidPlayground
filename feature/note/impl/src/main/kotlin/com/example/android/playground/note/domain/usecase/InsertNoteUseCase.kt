package com.example.android.playground.note.domain.usecase

import com.example.android.playground.note.domain.model.Note
import com.example.android.playground.note.domain.repository.NoteRepository
import javax.inject.Inject

class InsertNoteUseCase
    @Inject
    constructor(
        private val repository: NoteRepository,
    ) {
        suspend operator fun invoke(
            title: String,
            content: String,
        ): Long =
            repository.insertNote(
                Note(
                    title = title,
                    content = content,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                ),
            )
    }
