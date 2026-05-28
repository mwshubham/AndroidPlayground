package com.example.android.playground.deviceclassifier.presentation.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.deviceclassifier.domain.model.DeviceSpec
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeviceSpecCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun spec_withRamAboveGbThreshold_displaysGbFormat() {
        composeTestRule.setContent {
            AppTheme {
                DeviceSpecCard(spec = DeviceSpec(ramMb = 6_144L, cpuCores = 8, apiLevel = 33))
            }
        }

        composeTestRule.onNodeWithText("6.0 GB").assertIsDisplayed()
    }

    @Test
    fun spec_withRamBelowGbThreshold_displaysMbFormat() {
        composeTestRule.setContent {
            AppTheme {
                DeviceSpecCard(spec = DeviceSpec(ramMb = 512L, cpuCores = 4, apiLevel = 30))
            }
        }

        composeTestRule.onNodeWithText("512 MB").assertIsDisplayed()
    }

    @Test
    fun spec_displaysCpuCoresValue() {
        composeTestRule.setContent {
            AppTheme {
                DeviceSpecCard(spec = DeviceSpec(ramMb = 4_096L, cpuCores = 8, apiLevel = 33))
            }
        }

        composeTestRule.onNodeWithText("8").assertIsDisplayed()
    }

    @Test
    fun spec_displaysApiLevelValue() {
        composeTestRule.setContent {
            AppTheme {
                DeviceSpecCard(spec = DeviceSpec(ramMb = 4_096L, cpuCores = 8, apiLevel = 33))
            }
        }

        composeTestRule.onNodeWithText("33").assertIsDisplayed()
    }

    @Test
    fun spec_withExactlyOneGb_displaysOnePointZeroGb() {
        composeTestRule.setContent {
            AppTheme {
                DeviceSpecCard(spec = DeviceSpec(ramMb = 1_024L, cpuCores = 4, apiLevel = 29))
            }
        }

        composeTestRule.onNodeWithText("1.0 GB").assertIsDisplayed()
    }
}
