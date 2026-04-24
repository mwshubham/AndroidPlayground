package com.example.android.playground.roomdatabase.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * **@Database** — the Room database for the Library sample.
 *
 * Key concepts:
 * - Lists ALL @Entity classes in the `entities` array — Room generates the CREATE TABLE SQL.
 * - **@TypeConverters** — registers [GenreTypeConverter] so Room knows how to persist
 *   [List<String>] fields found in [BookEntity].
 * - `exportSchema = false` is intentional for this demo module (no migration testing needed).
 */
@Database(
    entities = [
        AuthorEntity::class,
        BookEntity::class,
        TagEntity::class,
        BookTagCrossRef::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(GenreTypeConverter::class)
abstract class LibraryDatabase : RoomDatabase() {
    abstract fun libraryDao(): LibraryDao

    companion object {
        const val DATABASE_NAME = "library"
    }
}
