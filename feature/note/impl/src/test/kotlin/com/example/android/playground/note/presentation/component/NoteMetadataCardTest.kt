package com.example.android.playground.note.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class NoteMetadataCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun differentTimes() {
        paparazzi.snapshot {
            AppTheme {
                NoteMetadataCard(
                    createdAtFormatted = "Jan 15, 2025 09:30",
                    updatedAtFormatted = "May 20, 2025 14:45",
                )
            }
        }
    }

    @Test
    fun sameCreatedAndUpdatedTime() {
        paparazzi.snapshot {
            AppTheme {
                NoteMetadataCard(
                    createdAtFormatted = "May 29, 2026 10:00",
                    updatedAtFormatted = "May 29, 2026 10:00",
                )
            }
        }
    }
}
