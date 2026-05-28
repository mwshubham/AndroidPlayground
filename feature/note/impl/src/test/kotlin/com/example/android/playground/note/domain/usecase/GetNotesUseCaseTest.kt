package com.example.android.playground.note.domain.usecase

import com.example.android.playground.note.domain.model.Note
import com.example.android.playground.note.domain.repository.NoteRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetNotesUseCaseTest {
    private val repository: NoteRepository = mockk()
    private lateinit var useCase: GetNotesUseCase

    private val sampleNotes =
        listOf(
            Note(id = 1L, title = "Note 1", content = "Content 1", createdAt = 1000L, updatedAt = 1000L),
            Note(id = 2L, title = "Note 2", content = "Content 2", createdAt = 2000L, updatedAt = 2000L),
        )

    @Before
    fun setUp() {
        useCase = GetNotesUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository and returns its flow`() =
        runTest {
            every { repository.getAllNotes() } returns flowOf(sampleNotes)

            val result = useCase().toList()

            assertEquals(listOf(sampleNotes), result)
            verify(exactly = 1) { repository.getAllNotes() }
        }

    @Test
    fun `invoke returns empty flow when repository has no notes`() =
        runTest {
            every { repository.getAllNotes() } returns flowOf(emptyList())

            val result = useCase().toList()

            assertEquals(listOf(emptyList<Note>()), result)
        }
}
