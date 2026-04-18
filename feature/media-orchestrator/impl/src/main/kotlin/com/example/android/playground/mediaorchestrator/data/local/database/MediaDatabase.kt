package com.example.android.playground.mediaorchestrator.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.playground.mediaorchestrator.data.local.dao.MediaItemDao
import com.example.android.playground.mediaorchestrator.data.local.entity.MediaItemEntity

@Database(
    entities = [MediaItemEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class MediaDatabase : RoomDatabase() {
    abstract fun mediaItemDao(): MediaItemDao

    companion object {
        const val DATABASE_NAME = "media_orchestrator"
    }
}
