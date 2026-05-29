package com.example.android.playground.note.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class NoteErrorCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun errorWithDefaultDismissText() {
        paparazzi.snapshot {
            AppTheme {
                NoteErrorCard(
                    errorMessage = "Failed to save note. Please try again.",
                    onDismiss = {},
                )
            }
        }
    }

    @Test
    fun errorWithCustomDismissText() {
        paparazzi.snapshot {
            AppTheme {
                NoteErrorCard(
                    errorMessage = "Network error occurred.",
                    dismissText = "Dismiss",
                    onDismiss = {},
                )
            }
        }
    }
}
