package com.example.android.playground.userinitiatedservice.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.playground.userinitiatedservice.data.local.dao.TransferItemDao
import com.example.android.playground.userinitiatedservice.data.local.entity.TransferItemEntity

@Database(
    entities = [TransferItemEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class TransferDatabase : RoomDatabase() {
    abstract fun transferItemDao(): TransferItemDao

    companion object {
        const val DATABASE_NAME = "transfer_database"
    }
}
