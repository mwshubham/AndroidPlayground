package com.example.android.playground.tictactoe.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.tictactoe.domain.model.GameMode
import org.junit.Rule
import org.junit.Test

class GameModePickerTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun playerVsAiSelected() {
        paparazzi.snapshot {
            AppTheme {
                GameModePicker(
                    selectedMode = GameMode.PlayerVsAi,
                    onModeSelected = {},
                )
            }
        }
    }

    @Test
    fun playerVsPlayerSelected() {
        paparazzi.snapshot {
            AppTheme {
                GameModePicker(
                    selectedMode = GameMode.PlayerVsPlayer,
                    onModeSelected = {},
                )
            }
        }
    }
}
