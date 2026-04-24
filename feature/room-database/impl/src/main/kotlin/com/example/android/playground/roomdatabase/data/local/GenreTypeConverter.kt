package com.example.android.playground.roomdatabase.data.local

import androidx.room.TypeConverter

/**
 * **@TypeConverter** — teaches Room how to persist a type it cannot store natively.
 *
 * Room understands primitives and Strings, but not [List<String>].
 * These two functions convert between a comma-separated String (stored in the DB column)
 * and a [List<String>] (used in Kotlin code).
 *
 * Registered at the database level via `@TypeConverters(GenreTypeConverter::class)`.
 */
class GenreTypeConverter {
    @TypeConverter
    fun fromGenreList(genres: List<String>): String = genres.joinToString(",")

    @TypeConverter
    fun toGenreList(genresString: String): List<String> =
        if (genresString.isBlank()) emptyList() else genresString.split(",")
}
