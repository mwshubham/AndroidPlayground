package com.example.android.playground.deviceclassifier.presentation.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.deviceclassifier.domain.model.DeviceTier
import com.example.android.playground.deviceclassifier.presentation.intent.DeviceClassifierIntent
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SimulationPanelTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun panel_showsSimulateParametersHeader() {
        composeTestRule.setContent {
            AppTheme {
                SimulationPanel(
                    simulatedRamMb = 4_096L,
                    simulatedCpuCores = 4,
                    effectiveTier = DeviceTier.MEDIUM,
                    isResetEnabled = false,
                    onIntent = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Simulate Parameters").assertIsDisplayed()
    }

    @Test
    fun resetEnabled_showsResetButton() {
        composeTestRule.setContent {
            AppTheme {
                SimulationPanel(
                    simulatedRamMb = 4_096L,
                    simulatedCpuCores = 4,
                    effectiveTier = DeviceTier.MEDIUM,
                    isResetEnabled = true,
                    onIntent = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Reset to device").assertIsDisplayed()
    }

    @Test
    fun resetDisabled_hidesResetButton() {
        composeTestRule.setContent {
            AppTheme {
                SimulationPanel(
                    simulatedRamMb = 4_096L,
                    simulatedCpuCores = 4,
                    effectiveTier = DeviceTier.MEDIUM,
                    isResetEnabled = false,
                    onIntent = {},
                )
            }
        }

        assertTrue(
            composeTestRule
                .onAllNodesWithText("Reset to device")
                .fetchSemanticsNodes()
                .isEmpty(),
        )
    }

    @Test
    fun resetButtonClick_firesResetToDeviceDefaultsIntent() {
        val capturedIntents = mutableListOf<DeviceClassifierIntent>()

        composeTestRule.setContent {
            AppTheme {
                SimulationPanel(
                    simulatedRamMb = 4_096L,
                    simulatedCpuCores = 4,
                    effectiveTier = DeviceTier.MEDIUM,
                    isResetEnabled = true,
                    onIntent = { capturedIntents.add(it) },
                )
            }
        }

        composeTestRule.onNodeWithText("Reset to device").performClick()

        assertTrue(capturedIntents.contains(DeviceClassifierIntent.ResetToDeviceDefaults))
    }
}
