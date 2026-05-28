package com.example.android.playground.tictactoe.domain.repository

import com.example.android.playground.tictactoe.domain.model.GameResult
import kotlinx.coroutines.flow.Flow

interface TicTacToeRepository {
    suspend fun saveGameResult(result: GameResult)

    fun getGameHistory(): Flow<List<GameResult>>
}
