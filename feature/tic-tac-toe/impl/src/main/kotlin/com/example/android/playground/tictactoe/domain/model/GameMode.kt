package com.example.android.playground.tictactoe.domain.model

sealed interface GameMode {
    data object PlayerVsPlayer : GameMode

    data object PlayerVsAi : GameMode
}
