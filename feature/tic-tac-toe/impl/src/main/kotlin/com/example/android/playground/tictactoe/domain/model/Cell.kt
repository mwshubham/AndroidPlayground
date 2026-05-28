package com.example.android.playground.tictactoe.domain.model

sealed interface Cell {
    data object Empty : Cell

    data class Occupied(
        val player: Player,
    ) : Cell
}
