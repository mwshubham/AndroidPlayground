package com.example.android.playground.tictactoe.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.tictactoe.domain.model.Player
import com.example.android.playground.tictactoe.domain.model.Symbol
import org.junit.Rule
import org.junit.Test

class PlayerIndicatorTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun playerXTurn() {
        paparazzi.snapshot {
            AppTheme {
                PlayerIndicator(
                    currentPlayer = Player(name = "Alice", symbol = Symbol.X),
                    isAiThinking = false,
                )
            }
        }
    }

    @Test
    fun playerOTurn() {
        paparazzi.snapshot {
            AppTheme {
                PlayerIndicator(
                    currentPlayer = Player(name = "Bob", symbol = Symbol.O),
                    isAiThinking = false,
                )
            }
        }
    }

    @Test
    fun aiThinking() {
        paparazzi.snapshot {
            AppTheme {
                PlayerIndicator(
                    currentPlayer = Player(name = "AI", symbol = Symbol.O),
                    isAiThinking = true,
                )
            }
        }
    }
}
