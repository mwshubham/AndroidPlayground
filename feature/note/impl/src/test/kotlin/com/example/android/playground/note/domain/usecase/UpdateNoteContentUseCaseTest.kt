package com.example.android.playground.note.domain.usecase

import com.example.android.playground.note.domain.repository.NoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateNoteContentUseCaseTest {
    private val repository: NoteRepository = mockk()
    private lateinit var useCase: UpdateNoteContentUseCase

    @Before
    fun setUp() {
        useCase = UpdateNoteContentUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository with correct id title and content`() =
        runTest {
            coEvery { repository.updateNoteContent(1L, "New Title", "New Content") } just runs

            useCase(id = 1L, title = "New Title", content = "New Content")

            coVerify(exactly = 1) { repository.updateNoteContent(1L, "New Title", "New Content") }
        }
}
