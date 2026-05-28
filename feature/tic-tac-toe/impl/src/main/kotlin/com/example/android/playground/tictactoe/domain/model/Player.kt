package com.example.android.playground.tictactoe.domain.model

enum class Symbol { X, O }

data class Player(
    val name: String,
    val symbol: Symbol,
)
