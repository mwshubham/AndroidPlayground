package com.example.android.playground.note.presentation.mapper

import com.example.android.playground.core.common.util.DateFormatter
import com.example.android.playground.note.domain.model.Note
import com.example.android.playground.note.presentation.model.NoteDetailUiModel
import com.example.android.playground.note.presentation.model.NoteListItemUiModel

/**
 * Mapper to convert domain Note to UI models with pre-formatted strings
 */
object NoteUiMapper {

    /**
     * Converts domain Note to NoteDetailUiModel for detailed view
     */
    fun toUiModel(note: Note): NoteDetailUiModel =
        NoteDetailUiModel(
            id = note.id,
            title = note.title,
            content = note.content,
            createdAtFormatted = DateFormatter.formatTimestamp(note.createdAt),
            updatedAtFormatted = DateFormatter.formatTimestamp(note.updatedAt),
        )

    /**
     * Converts domain Note to NoteListItemUiModel for list view
     */
    fun toListUiModel(note: Note): NoteListItemUiModel =
        NoteListItemUiModel(
            id = note.id,
            title = note.title,
            content = note.content,
            updatedAtFormatted = DateFormatter.formatTimestamp(note.updatedAt),
        )
}
