package com.example.android.playground.roomdatabase.data.local

import androidx.room.ColumnInfo

private const val CONTACT_INFO_COLUMN_EMAIL = "email"
private const val CONTACT_INFO_COLUMN_WEBSITE = "website"

/**
 * Plain data class used via @Embedded in [AuthorEntity].
 * Demonstrates Room's @Embedded annotation — embeds the fields of this class
 * directly into the parent table row (no separate table created).
 */
data class ContactInfo(
    @ColumnInfo(name = CONTACT_INFO_COLUMN_EMAIL)
    val email: String,
    @ColumnInfo(name = CONTACT_INFO_COLUMN_WEBSITE)
    val website: String,
)
