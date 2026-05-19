package com.example.android.playground.tictactoe.domain.usecase

import com.example.android.playground.tictactoe.domain.model.GameResult
import com.example.android.playground.tictactoe.domain.repository.TicTacToeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGameHistoryUseCase
    @Inject
    constructor(
        private val repository: TicTacToeRepository,
    ) {
        operator fun invoke(): Flow<List<GameResult>> = repository.getGameHistory()
    }
