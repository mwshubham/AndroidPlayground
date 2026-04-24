package com.example.android.playground.roomdatabase.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity

const val BOOK_TAG_CROSS_REF_TABLE_NAME = "book_tag_cross_ref"
const val BOOK_TAG_CROSS_REF_COLUMN_BOOK_ID = "book_id"
const val BOOK_TAG_CROSS_REF_COLUMN_TAG_ID = "tag_id"

/**
 * **Join / Junction table** for the many-to-many relationship between books and tags.
 *
 * Key concept: **Many-to-Many via join table** — a book can have many tags and a tag
 * can belong to many books. Room cannot model this directly, so we create an explicit
 * cross-reference entity. The composite primary key `(bookId, tagId)` ensures each
 * book-tag pair is unique.
 *
 * Used in [BookWithTagsRelation] via `@Relation(associateBy = Junction(...))`.
 */
@Entity(
    tableName = BOOK_TAG_CROSS_REF_TABLE_NAME,
    primaryKeys = [BOOK_TAG_CROSS_REF_COLUMN_BOOK_ID, BOOK_TAG_CROSS_REF_COLUMN_TAG_ID],
)
data class BookTagCrossRef(
    @ColumnInfo(name = BOOK_TAG_CROSS_REF_COLUMN_BOOK_ID)
    val bookId: Long,
    @ColumnInfo(name = BOOK_TAG_CROSS_REF_COLUMN_TAG_ID, index = true)
    val tagId: Long,
)
