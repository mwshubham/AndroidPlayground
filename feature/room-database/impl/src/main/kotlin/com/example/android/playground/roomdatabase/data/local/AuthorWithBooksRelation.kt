package com.example.android.playground.roomdatabase.data.local

import androidx.room.Embedded
import androidx.room.Relation

/**
 * **One-to-Many @Relation** — fetches an author together with all their books in a single query.
 *
 * Key concept: Room resolves this with two SQL queries under the hood:
 * 1. Fetch parent rows (authors)
 * 2. Fetch all matching child rows (books where author_id IN (...))
 * Then it assembles the result in-memory. The `@Transaction` annotation on the DAO query
 * ensures both queries happen atomically so the result is never inconsistent.
 */
data class AuthorWithBooksRelation(
    @Embedded val author: AuthorEntity,
    @Relation(
        parentColumn = AUTHOR_COLUMN_ID,
        entityColumn = BOOK_COLUMN_AUTHOR_ID,
    )
    val books: List<BookEntity>,
)
