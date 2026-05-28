package com.example.android.playground.tictactoe.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GameResultEntity::class], version = 1, exportSchema = false)
abstract class TicTacToeDatabase : RoomDatabase() {
    abstract fun gameResultDao(): GameResultDao
}
