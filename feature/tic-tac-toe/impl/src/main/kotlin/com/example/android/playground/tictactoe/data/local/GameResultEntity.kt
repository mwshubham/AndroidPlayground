package com.example.android.playground.tictactoe.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_results")
data class GameResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val winnerName: String?,
    val mode: String,
    val totalMoves: Int,
    val playedAt: Long,
)
