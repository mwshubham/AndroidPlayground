package com.example.android.playground.websocket.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.websocket.presentation.state.BtcTickerUiModel
import org.junit.Rule
import org.junit.Test

class TickerHistoryItemTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun positiveChange() {
        paparazzi.snapshot {
            AppTheme {
                TickerHistoryItem(
                    tick =
                        BtcTickerUiModel(
                            price = "67,432.10",
                            priceChange = "+1,234.50",
                            priceChangePercent = "+1.87%",
                            isPositive = true,
                            formattedTime = "14:32:01",
                        ),
                )
            }
        }
    }

    @Test
    fun negativeChange() {
        paparazzi.snapshot {
            AppTheme {
                TickerHistoryItem(
                    tick =
                        BtcTickerUiModel(
                            price = "65,200.00",
                            priceChange = "-1,100.50",
                            priceChangePercent = "-1.66%",
                            isPositive = false,
                            formattedTime = "14:30:45",
                        ),
                )
            }
        }
    }
}
