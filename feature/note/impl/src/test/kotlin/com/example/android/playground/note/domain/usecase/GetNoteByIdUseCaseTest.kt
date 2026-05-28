package com.example.android.playground.note.domain.usecase

import com.example.android.playground.note.domain.model.Note
import com.example.android.playground.note.domain.repository.NoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetNoteByIdUseCaseTest {
    private val repository: NoteRepository = mockk()
    private lateinit var useCase: GetNoteByIdUseCase

    private val note = Note(id = 1L, title = "Title", content = "Body", createdAt = 1000L, updatedAt = 1000L)

    @Before
    fun setUp() {
        useCase = GetNoteByIdUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository and returns note`() =
        runTest {
            coEvery { repository.getNoteById(1L) } returns note

            val result = useCase(1L)

            assertEquals(note, result)
            coVerify(exactly = 1) { repository.getNoteById(1L) }
        }

    @Test
    fun `invoke returns null when note is not found`() =
        runTest {
            coEvery { repository.getNoteById(999L) } returns null

            assertNull(useCase(999L))
        }
}
