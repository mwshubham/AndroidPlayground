package com.example.android.playground.tictactoe.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object TicTacToeSetupRoute : NavKey

@Serializable
data class TicTacToeGameRoute(
    val mode: String,
    val player1Name: String,
    val player2Name: String,
) : NavKey
