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

class DeleteNoteUseCaseTest {
    private val repository: NoteRepository = mockk()
    private lateinit var useCase: DeleteNoteUseCase

    @Before
    fun setUp() {
        useCase = DeleteNoteUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository with correct id`() =
        runTest {
            coEvery { repository.deleteNoteById(5L) } just runs

            useCase(5L)

            coVerify(exactly = 1) { repository.deleteNoteById(5L) }
        }
}
