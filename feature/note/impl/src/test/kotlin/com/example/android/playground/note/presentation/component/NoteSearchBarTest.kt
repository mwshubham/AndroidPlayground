package com.example.android.playground.note.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class NoteSearchBarTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun emptyQuery() {
        paparazzi.snapshot {
            AppTheme {
                NoteSearchBar(
                    searchQuery = "",
                    onSearchQueryChange = {},
                )
            }
        }
    }

    @Test
    fun filledQuery() {
        paparazzi.snapshot {
            AppTheme {
                NoteSearchBar(
                    searchQuery = "meeting notes",
                    onSearchQueryChange = {},
                )
            }
        }
    }
}
