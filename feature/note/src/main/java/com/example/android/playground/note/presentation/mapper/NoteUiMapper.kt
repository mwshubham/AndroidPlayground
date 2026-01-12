package com.example.android.playground.note.presentation.mapper

import com.example.android.playground.core.common.util.DateFormatter
import com.example.android.playground.note.domain.model.Note
import com.example.android.playground.note.presentation.model.NoteUiModel
import com.example.android.playground.note.presentation.model.NoteListUiModel

/**
 * Mapper to convert domain Note to UI models with pre-formatted strings
 */
object NoteUiMapper {

    /**
     * Converts domain Note to NoteUiModel for detailed view
     */
    fun toUiModel(note: Note): NoteUiModel =
        NoteUiModel(
            id = note.id,
            title = note.title,
            content = note.content,
            createdAtFormatted = DateFormatter.formatTimestamp(note.createdAt),
            updatedAtFormatted = DateFormatter.formatTimestamp(note.updatedAt),
        )

    /**
     * Converts domain Note to NoteListUiModel for list view
     */
    fun toListUiModel(note: Note): NoteListUiModel =
        NoteListUiModel(
            id = note.id,
            title = note.title,
            content = note.content,
            updatedAtFormatted = DateFormatter.formatTimestamp(note.updatedAt),
        )
}
