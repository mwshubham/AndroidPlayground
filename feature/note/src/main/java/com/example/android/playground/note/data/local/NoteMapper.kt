package com.example.android.playground.note.data.local

import com.example.android.playground.note.domain.model.Note

fun NoteEntity.toDomainModel(): Note =
    Note(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun Note.toEntity(): NoteEntity =
    NoteEntity(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun List<NoteEntity>.toDomainModelList(): List<Note> = map { it.toDomainModel() }
