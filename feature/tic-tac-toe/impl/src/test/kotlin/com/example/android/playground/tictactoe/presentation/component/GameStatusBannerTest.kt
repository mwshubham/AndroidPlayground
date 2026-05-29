package com.example.android.playground.tictactoe.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.tictactoe.domain.model.GameStatus
import com.example.android.playground.tictactoe.domain.model.Player
import com.example.android.playground.tictactoe.domain.model.Symbol
import org.junit.Rule
import org.junit.Test

class GameStatusBannerTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    private val playerX = Player(name = "Alice", symbol = Symbol.X)

    @Test
    fun wonStatus() {
        paparazzi.snapshot {
            AppTheme {
                GameStatusBanner(
                    status =
                        GameStatus.Won(
                            winner = playerX,
                            winningCells = listOf(0 to 0, 0 to 1, 0 to 2),
                        ),
                )
            }
        }
    }

    @Test
    fun drawStatus() {
        paparazzi.snapshot {
            AppTheme {
                GameStatusBanner(status = GameStatus.Draw)
            }
        }
    }
}
