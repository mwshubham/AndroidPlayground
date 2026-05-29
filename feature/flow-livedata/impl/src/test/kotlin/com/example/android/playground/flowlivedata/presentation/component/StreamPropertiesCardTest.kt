package com.example.android.playground.flowlivedata.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.flowlivedata.presentation.model.StreamType
import org.junit.Rule
import org.junit.Test

class StreamPropertiesCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun stateFlowProperties() {
        paparazzi.snapshot {
            AppTheme {
                StreamPropertiesCard(streamType = StreamType.STATE_FLOW)
            }
        }
    }

    @Test
    fun sharedFlowProperties() {
        paparazzi.snapshot {
            AppTheme {
                StreamPropertiesCard(streamType = StreamType.SHARED_FLOW)
            }
        }
    }
}
