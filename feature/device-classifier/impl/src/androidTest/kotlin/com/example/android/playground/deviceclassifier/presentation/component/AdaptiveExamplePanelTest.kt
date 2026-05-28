package com.example.android.playground.deviceclassifier.presentation.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.deviceclassifier.domain.model.DeviceTier
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AdaptiveExamplePanelTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun lowTier_showsLowTierTitle() {
        composeTestRule.setContent {
            AppTheme {
                AdaptiveExamplePanel(tier = DeviceTier.LOW)
            }
        }

        composeTestRule.onNodeWithText("Adaptive UI — LOW tier").assertIsDisplayed()
    }

    @Test
    fun mediumTier_showsMediumTierTitle() {
        composeTestRule.setContent {
            AppTheme {
                AdaptiveExamplePanel(tier = DeviceTier.MEDIUM)
            }
        }

        composeTestRule.onNodeWithText("Adaptive UI — MEDIUM tier").assertIsDisplayed()
    }

    @Test
    fun highTier_showsHighTierTitle() {
        composeTestRule.setContent {
            AppTheme {
                AdaptiveExamplePanel(tier = DeviceTier.HIGH)
            }
        }

        composeTestRule.onNodeWithText("Adaptive UI — HIGH tier").assertIsDisplayed()
    }
}
