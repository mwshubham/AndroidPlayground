package com.example.android.playground.flowlivedata.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.flowlivedata.presentation.model.StreamType
import com.example.android.playground.flowlivedata.presentation.state.FlowLiveDataState
import org.junit.Rule
import org.junit.Test

class FlowLiveDataContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun stateFlowTab() {
        paparazzi.snapshot {
            AppTheme {
                FlowLiveDataContent(
                    state = FlowLiveDataState(selectedTab = StreamType.STATE_FLOW),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun sharedFlowTab() {
        paparazzi.snapshot {
            AppTheme {
                FlowLiveDataContent(
                    state = FlowLiveDataState(selectedTab = StreamType.SHARED_FLOW),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun liveDataTab() {
        paparazzi.snapshot {
            AppTheme {
                FlowLiveDataContent(
                    state = FlowLiveDataState(selectedTab = StreamType.LIVE_DATA),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun channelTab() {
        paparazzi.snapshot {
            AppTheme {
                FlowLiveDataContent(
                    state = FlowLiveDataState(selectedTab = StreamType.CHANNEL),
                    onIntent = {},
                )
            }
        }
    }
}
