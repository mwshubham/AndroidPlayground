package com.example.android.playground.tictactoe.domain.model

data class GameResult(
    val id: Long = 0L,
    val winnerName: String?,
    val mode: String,
    val totalMoves: Int,
    val playedAt: Long,
)
