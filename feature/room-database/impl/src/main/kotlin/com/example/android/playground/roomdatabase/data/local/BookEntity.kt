package com.example.android.playground.roomdatabase.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

const val BOOK_TABLE_NAME = "books"
const val BOOK_COLUMN_ID = "id"
const val BOOK_COLUMN_TITLE = "title"
const val BOOK_COLUMN_AUTHOR_ID = "author_id"
const val BOOK_COLUMN_PUBLISH_YEAR = "publish_year"
const val BOOK_COLUMN_GENRES = "genres"

/**
 * Room @Entity — maps to the "books" table.
 *
 * Key concepts demonstrated:
 * - **@ForeignKey** — enforces referential integrity linking each book to an author.
 *   CASCADE delete means removing an author also removes their books.
 * - **@Index** — speeds up queries filtering/joining on authorId.
 * - **@TypeConverter (via GenreTypeConverter)** — [genres] is a [List<String>],
 *   which Room can't store natively; the converter serialises it as a comma-separated String.
 */
@Entity(
    tableName = BOOK_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = AuthorEntity::class,
            parentColumns = [AUTHOR_COLUMN_ID],
            childColumns = [BOOK_COLUMN_AUTHOR_ID],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = [BOOK_COLUMN_AUTHOR_ID])],
)
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = BOOK_COLUMN_ID)
    val id: Long = 0,
    @ColumnInfo(name = BOOK_COLUMN_TITLE)
    val title: String,
    @ColumnInfo(name = BOOK_COLUMN_AUTHOR_ID)
    val authorId: Long,
    @ColumnInfo(name = BOOK_COLUMN_PUBLISH_YEAR)
    val publishYear: Int,
    // List<String> stored as a comma-separated String via GenreTypeConverter
    @ColumnInfo(name = BOOK_COLUMN_GENRES)
    val genres: List<String>,
)
