package com.example.android.playground.flowlivedata.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.flowlivedata.presentation.model.StreamType
import org.junit.Rule
import org.junit.Test

class StreamTypeTabRowTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun stateFlowSelected() {
        paparazzi.snapshot {
            AppTheme {
                StreamTypeTabRow(selectedTab = StreamType.STATE_FLOW, onTabSelected = {})
            }
        }
    }

    @Test
    fun channelSelected() {
        paparazzi.snapshot {
            AppTheme {
                StreamTypeTabRow(selectedTab = StreamType.CHANNEL, onTabSelected = {})
            }
        }
    }
}
