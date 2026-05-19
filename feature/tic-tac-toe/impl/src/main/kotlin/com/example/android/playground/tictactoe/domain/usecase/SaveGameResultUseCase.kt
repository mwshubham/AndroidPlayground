package com.example.android.playground.tictactoe.domain.usecase

import com.example.android.playground.tictactoe.domain.model.GameResult
import com.example.android.playground.tictactoe.domain.repository.TicTacToeRepository
import javax.inject.Inject

class SaveGameResultUseCase
    @Inject
    constructor(
        private val repository: TicTacToeRepository,
    ) {
        suspend operator fun invoke(result: GameResult) = repository.saveGameResult(result)
    }
