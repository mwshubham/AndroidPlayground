package com.example.android.playground.roomdatabase.data.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

const val AUTHOR_TABLE_NAME = "authors"
const val AUTHOR_COLUMN_ID = "id"
const val AUTHOR_COLUMN_NAME = "name"

/**
 * Room @Entity — maps to the "authors" table.
 *
 * Key concept: **@Embedded** — [ContactInfo] fields are stored as flat columns
 * in this same table (email, website) rather than a separate table.
 */
@Entity(tableName = AUTHOR_TABLE_NAME)
data class AuthorEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = AUTHOR_COLUMN_ID)
    val id: Long = 0,
    @ColumnInfo(name = AUTHOR_COLUMN_NAME)
    val name: String,
    @Embedded
    val contact: ContactInfo,
)
