package com.example.android.playground.roomdatabase.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

private const val LIBRARY_DAO_QUERY_ALL_AUTHORS_WITH_BOOKS =
    "SELECT * FROM $AUTHOR_TABLE_NAME ORDER BY $AUTHOR_COLUMN_NAME ASC"
private const val LIBRARY_DAO_QUERY_ALL_BOOKS_WITH_TAGS =
    "SELECT * FROM $BOOK_TABLE_NAME ORDER BY $BOOK_COLUMN_TITLE ASC"
private const val LIBRARY_DAO_QUERY_AUTHOR_COUNT =
    "SELECT COUNT(*) FROM $AUTHOR_TABLE_NAME"

/**
 * **@Dao** — Data Access Object for all library operations.
 *
 * Key concepts demonstrated:
 * - **@Transaction** — wraps multiple queries in a DB transaction; required when using
 *   @Relation so Room never reads a partially-updated set of rows.
 * - **Flow return type** — Room automatically re-emits when the underlying table changes,
 *   giving a reactive data stream with zero boilerplate.
 * - **@Insert** — Room generates the SQL; [OnConflictStrategy.IGNORE] silently skips
 *   duplicates (used for idempotent seeding).
 */
@Dao
interface LibraryDao {
    // ── Reads ────────────────────────────────────────────────────────────────

    @Transaction
    @Query(LIBRARY_DAO_QUERY_ALL_AUTHORS_WITH_BOOKS)
    fun getAllAuthorsWithBooks(): Flow<List<AuthorWithBooksRelation>>

    @Transaction
    @Query(LIBRARY_DAO_QUERY_ALL_BOOKS_WITH_TAGS)
    fun getAllBooksWithTags(): Flow<List<BookWithTagsRelation>>

    @Query(LIBRARY_DAO_QUERY_AUTHOR_COUNT)
    suspend fun getAuthorCount(): Int

    // ── Inserts (for seeding) ─────────────────────────────────────────────────

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAuthor(author: AuthorEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBook(book: BookEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: TagEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBookTagCrossRef(crossRef: BookTagCrossRef)
}
