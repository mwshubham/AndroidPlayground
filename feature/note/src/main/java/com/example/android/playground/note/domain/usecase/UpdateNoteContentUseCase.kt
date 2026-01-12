package com.example.android.playground.note.domain.usecase

import com.example.android.playground.note.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateNoteContentUseCase
    @Inject
    constructor(
        private val repository: NoteRepository,
    ) {
        suspend operator fun invoke(id: Long, title: String, content: String) {
            repository.updateNoteContent(id, title, content)
        }
    }
