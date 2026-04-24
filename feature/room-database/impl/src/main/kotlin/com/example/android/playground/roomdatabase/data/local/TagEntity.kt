package com.example.android.playground.roomdatabase.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TAG_TABLE_NAME = "tags"
const val TAG_COLUMN_ID = "id"
const val TAG_COLUMN_NAME = "name"

/**
 * Room @Entity — maps to the "tags" table.
 * Used in a many-to-many relationship with books via [BookTagCrossRef].
 */
@Entity(tableName = TAG_TABLE_NAME)
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = TAG_COLUMN_ID)
    val id: Long = 0,
    @ColumnInfo(name = TAG_COLUMN_NAME)
    val name: String,
)
