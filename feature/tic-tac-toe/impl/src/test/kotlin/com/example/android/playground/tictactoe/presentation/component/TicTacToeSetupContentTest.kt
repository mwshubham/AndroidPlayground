package com.example.android.playground.tictactoe.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.tictactoe.domain.model.GameMode
import com.example.android.playground.tictactoe.presentation.state.TicTacToeSetupState
import org.junit.Rule
import org.junit.Test

class TicTacToeSetupContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun defaultEmptyState() {
        paparazzi.snapshot {
            AppTheme {
                TicTacToeSetupContent(
                    state = TicTacToeSetupState(),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun stateWithNamesAndStartEnabled() {
        paparazzi.snapshot {
            AppTheme {
                TicTacToeSetupContent(
                    state =
                        TicTacToeSetupState(
                            selectedMode = GameMode.PlayerVsPlayer,
                            player1Name = "Alice",
                            player2Name = "Bob",
                            gamesPlayed = 3,
                            isStartEnabled = true,
                        ),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun vsAiModeWithPlayer1Name() {
        paparazzi.snapshot {
            AppTheme {
                TicTacToeSetupContent(
                    state =
                        TicTacToeSetupState(
                            selectedMode = GameMode.PlayerVsAi,
                            player1Name = "Alice",
                            player2Name = "",
                            isStartEnabled = true,
                        ),
                    onIntent = {},
                )
            }
        }
    }
}
