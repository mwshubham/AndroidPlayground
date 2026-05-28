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
class TierBadgeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun lowTier_displaysLowLabel() {
        composeTestRule.setContent {
            AppTheme {
                TierBadge(tier = DeviceTier.LOW)
            }
        }

        composeTestRule.onNodeWithText("LOW").assertIsDisplayed()
    }

    @Test
    fun mediumTier_displaysMediumLabel() {
        composeTestRule.setContent {
            AppTheme {
                TierBadge(tier = DeviceTier.MEDIUM)
            }
        }

        composeTestRule.onNodeWithText("MEDIUM").assertIsDisplayed()
    }

    @Test
    fun highTier_displaysHighLabel() {
        composeTestRule.setContent {
            AppTheme {
                TierBadge(tier = DeviceTier.HIGH)
            }
        }

        composeTestRule.onNodeWithText("HIGH").assertIsDisplayed()
    }
}
