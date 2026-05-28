package com.example.android.playground.tictactoe.data.repository

import com.example.android.playground.tictactoe.data.local.GameResultDao
import com.example.android.playground.tictactoe.data.local.toDomain
import com.example.android.playground.tictactoe.data.local.toEntity
import com.example.android.playground.tictactoe.domain.model.GameResult
import com.example.android.playground.tictactoe.domain.repository.TicTacToeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TicTacToeRepositoryImpl
    @Inject
    constructor(
        private val dao: GameResultDao,
    ) : TicTacToeRepository {
        override suspend fun saveGameResult(result: GameResult) {
            dao.insert(result.toEntity())
        }

        override fun getGameHistory(): Flow<List<GameResult>> = dao.observeAll().map { entities -> entities.map { it.toDomain() } }
    }
