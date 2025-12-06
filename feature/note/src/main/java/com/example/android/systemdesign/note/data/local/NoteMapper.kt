package com.example.android.systemdesign.note.data.local

import com.example.android.systemdesign.note.domain.model.Note

fun NoteEntity.toDomainModel(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun List<NoteEntity>.toDomainModelList(): List<Note> {
    return map { it.toDomainModel() }
}
