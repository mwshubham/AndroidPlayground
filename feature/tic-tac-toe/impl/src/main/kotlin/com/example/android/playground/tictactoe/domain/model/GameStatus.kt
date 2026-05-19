package com.example.android.playground.tictactoe.domain.model

sealed interface GameStatus {
    data object InProgress : GameStatus

    data class Won(
        val winner: Player,
        val winningCells: List<Pair<Int, Int>>,
    ) : GameStatus

    data object Draw : GameStatus
}
