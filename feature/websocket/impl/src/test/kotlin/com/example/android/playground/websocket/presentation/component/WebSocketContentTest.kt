package com.example.android.playground.websocket.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.websocket.presentation.state.BtcTickerUiModel
import com.example.android.playground.websocket.presentation.state.WebSocketConnectionStatus
import com.example.android.playground.websocket.presentation.state.WebSocketState
import org.junit.Rule
import org.junit.Test

class WebSocketContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun disconnectedState() {
        paparazzi.snapshot {
            AppTheme {
                WebSocketContent(
                    state = WebSocketState(),
                    onIntent = {},
                    onNavigateBack = {},
                )
            }
        }
    }

    @Test
    fun connectedWithData() {
        paparazzi.snapshot {
            AppTheme {
                WebSocketContent(
                    state =
                        WebSocketState(
                            connectionStatus = WebSocketConnectionStatus.Connected,
                            price = "67,432.10",
                            priceChange = "+1,234.50",
                            priceChangePercent = "+1.87%",
                            highPrice = "68,100.00",
                            lowPrice = "66,200.00",
                            isPositive = true,
                            recentTicks =
                                listOf(
                                    BtcTickerUiModel(
                                        price = "67,432.10",
                                        priceChange = "+1,234.50",
                                        priceChangePercent = "+1.87%",
                                        isPositive = true,
                                        formattedTime = "14:32:01",
                                    ),
                                    BtcTickerUiModel(
                                        price = "67,100.00",
                                        priceChange = "+902.40",
                                        priceChangePercent = "+1.36%",
                                        isPositive = true,
                                        formattedTime = "14:31:58",
                                    ),
                                ),
                        ),
                    onIntent = {},
                    onNavigateBack = {},
                )
            }
        }
    }

    @Test
    fun errorState() {
        paparazzi.snapshot {
            AppTheme {
                WebSocketContent(
                    state = WebSocketState(connectionStatus = WebSocketConnectionStatus.Error),
                    onIntent = {},
                    onNavigateBack = {},
                )
            }
        }
    }
}
