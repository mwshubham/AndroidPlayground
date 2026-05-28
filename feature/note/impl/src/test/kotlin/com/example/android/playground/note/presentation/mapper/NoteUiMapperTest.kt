package com.example.android.playground.note.presentation.mapper

import com.example.android.playground.note.domain.model.Note
import org.junit.Assert.assertEquals
import org.junit.Test

class NoteUiMapperTest {
    private val note =
        Note(
            id = 7L,
            title = "My Note",
            content = "Some content",
            createdAt = 0L,
            updatedAt = 0L,
        )

    @Test
    fun `toListUiModel maps id title content and formats updatedAt`() {
        val result = NoteUiMapper.toListUiModel(note)

        assertEquals(note.id, result.id)
        assertEquals(note.title, result.title)
        assertEquals(note.content, result.content)
        // The formatted date should be non-blank; exact value is locale-dependent.
        assert(result.updatedAtFormatted.isNotBlank())
    }

    @Test
    fun `toUiModel maps id title content and formats both timestamps`() {
        val result = NoteUiMapper.toUiModel(note)

        assertEquals(note.id, result.id)
        assertEquals(note.title, result.title)
        assertEquals(note.content, result.content)
        assert(result.createdAtFormatted.isNotBlank())
        assert(result.updatedAtFormatted.isNotBlank())
    }

    @Test
    fun `toListUiModel and toUiModel produce updatedAt formatted from same timestamp`() {
        val listModel = NoteUiMapper.toListUiModel(note)
        val detailModel = NoteUiMapper.toUiModel(note)

        assertEquals(listModel.updatedAtFormatted, detailModel.updatedAtFormatted)
    }
}
