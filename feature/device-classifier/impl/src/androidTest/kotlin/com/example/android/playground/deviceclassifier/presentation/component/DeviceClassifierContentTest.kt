package com.example.android.playground.deviceclassifier.presentation.component

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.deviceclassifier.domain.model.DeviceSpec
import com.example.android.playground.deviceclassifier.domain.model.DeviceTier
import com.example.android.playground.deviceclassifier.presentation.intent.DeviceClassifierIntent
import com.example.android.playground.deviceclassifier.presentation.state.DeviceClassifierState
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeviceClassifierContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val loadedState =
        DeviceClassifierState(
            actualSpec = DeviceSpec(ramMb = 6_144L, cpuCores = 8, apiLevel = 33),
            simulatedRamMb = 6_144L,
            simulatedCpuCores = 8,
            effectiveTier = DeviceTier.HIGH,
        )

    @Test
    fun loadingState_showsProgressIndicator() {
        composeTestRule.setContent {
            AppTheme {
                DeviceClassifierContent(
                    state = DeviceClassifierState(isLoading = true),
                    onIntent = {},
                )
            }
        }

        composeTestRule
            .onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertIsDisplayed()
    }

    @Test
    fun errorState_showsErrorMessage() {
        val errorMessage = "Could not read device info"

        composeTestRule.setContent {
            AppTheme {
                DeviceClassifierContent(
                    state = DeviceClassifierState(error = errorMessage),
                    onIntent = {},
                )
            }
        }

        composeTestRule
            .onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    @Test
    fun successState_showsTopBarWithTitle() {
        composeTestRule.setContent {
            AppTheme {
                DeviceClassifierContent(state = loadedState, onIntent = {})
            }
        }

        composeTestRule
            .onNodeWithText("Device Classifier")
            .assertIsDisplayed()
    }

    @Test
    fun successState_showsActualDeviceSectionLabel() {
        composeTestRule.setContent {
            AppTheme {
                DeviceClassifierContent(state = loadedState, onIntent = {})
            }
        }

        composeTestRule
            .onNodeWithText("Actual Device")
            .assertIsDisplayed()
    }

    @Test
    fun backButton_click_triggersNavigateBackIntent() {
        val capturedIntents = mutableListOf<DeviceClassifierIntent>()

        composeTestRule.setContent {
            AppTheme {
                DeviceClassifierContent(
                    state = loadedState,
                    onIntent = { capturedIntents.add(it) },
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Back")
            .performClick()

        assertTrue(capturedIntents.any { it is DeviceClassifierIntent.NavigateBack })
    }
}
