package com.example.android.playground.note.data.local

import com.example.android.playground.note.domain.model.Note
import org.junit.Assert.assertEquals
import org.junit.Test

class NoteMapperTest {
    private val entity =
        NoteEntity(
            id = 1L,
            title = "Test Title",
            content = "Test Content",
            createdAt = 1000L,
            updatedAt = 2000L,
        )

    private val domain =
        Note(
            id = 1L,
            title = "Test Title",
            content = "Test Content",
            createdAt = 1000L,
            updatedAt = 2000L,
        )

    @Test
    fun `NoteEntity toDomainModel maps all fields correctly`() {
        val result = entity.toDomainModel()

        assertEquals(entity.id, result.id)
        assertEquals(entity.title, result.title)
        assertEquals(entity.content, result.content)
        assertEquals(entity.createdAt, result.createdAt)
        assertEquals(entity.updatedAt, result.updatedAt)
    }

    @Test
    fun `Note toEntity maps all fields correctly`() {
        val result = domain.toEntity()

        assertEquals(domain.id, result.id)
        assertEquals(domain.title, result.title)
        assertEquals(domain.content, result.content)
        assertEquals(domain.createdAt, result.createdAt)
        assertEquals(domain.updatedAt, result.updatedAt)
    }

    @Test
    fun `NoteEntity toDomainModel and Note toEntity round-trip preserves all values`() {
        val roundTripped = entity.toDomainModel().toEntity()

        assertEquals(entity, roundTripped)
    }

    @Test
    fun `List of NoteEntity toDomainModelList maps each element`() {
        val entities =
            listOf(
                entity,
                entity.copy(id = 2L, title = "Second"),
            )

        val result = entities.toDomainModelList()

        assertEquals(2, result.size)
        assertEquals("Test Title", result[0].title)
        assertEquals("Second", result[1].title)
    }
}
