package com.example.android.playground.tictactoe.data.local

import com.example.android.playground.tictactoe.domain.model.GameResult

fun GameResultEntity.toDomain(): GameResult =
    GameResult(
        id = id,
        winnerName = winnerName,
        mode = mode,
        totalMoves = totalMoves,
        playedAt = playedAt,
    )

fun GameResult.toEntity(): GameResultEntity =
    GameResultEntity(
        id = id,
        winnerName = winnerName,
        mode = mode,
        totalMoves = totalMoves,
        playedAt = playedAt,
    )
