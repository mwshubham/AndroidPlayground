package com.example.android.playground.note.domain.usecase

import com.example.android.playground.note.domain.model.Note
import com.example.android.playground.note.domain.repository.NoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class InsertNoteUseCaseTest {
    private val repository: NoteRepository = mockk()
    private lateinit var useCase: InsertNoteUseCase

    @Before
    fun setUp() {
        useCase = InsertNoteUseCase(repository)
    }

    @Test
    fun `invoke creates Note with supplied title and content and delegates to repository`() =
        runTest {
            val capturedNote = slot<Note>()
            coEvery { repository.insertNote(capture(capturedNote)) } returns 42L

            val result = useCase("My Title", "My Content")

            assertEquals(42L, result)
            assertEquals("My Title", capturedNote.captured.title)
            assertEquals("My Content", capturedNote.captured.content)
        }

    @Test
    fun `invoke delegates to repository exactly once`() =
        runTest {
            coEvery { repository.insertNote(any()) } returns 1L

            useCase("Title", "Content")

            coVerify(exactly = 1) { repository.insertNote(any()) }
        }
}
