package com.example.android.playground.roomdatabase.data.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * **Many-to-Many @Relation via Junction** — fetches a book together with all its tags.
 *
 * Key concept: Room uses [BookTagCrossRef] as the junction (join) table to resolve
 * which tags belong to which book. The `associateBy = Junction(...)` tells Room:
 * "look up the cross-ref table to connect books.id → tags.id".
 *
 * Must be used with `@Transaction` in the DAO to ensure a consistent read.
 */
data class BookWithTagsRelation(
    @Embedded val book: BookEntity,
    @Relation(
        parentColumn = BOOK_COLUMN_ID,
        entityColumn = TAG_COLUMN_ID,
        associateBy = Junction(
            value = BookTagCrossRef::class,
            parentColumn = BOOK_TAG_CROSS_REF_COLUMN_BOOK_ID,
            entityColumn = BOOK_TAG_CROSS_REF_COLUMN_TAG_ID,
        ),
    )
    val tags: List<TagEntity>,
)
