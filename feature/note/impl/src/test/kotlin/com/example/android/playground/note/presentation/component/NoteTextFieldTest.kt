package com.example.android.playground.note.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class NoteTextFieldTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun singleLineEmpty() {
        paparazzi.snapshot {
            AppTheme {
                NoteTextField(
                    value = "",
                    label = "Title",
                    onValueChange = {},
                )
            }
        }
    }

    @Test
    fun singleLineFilled() {
        paparazzi.snapshot {
            AppTheme {
                NoteTextField(
                    value = "My note title",
                    label = "Title",
                    onValueChange = {},
                )
            }
        }
    }

    @Test
    fun multilineFilled() {
        paparazzi.snapshot {
            AppTheme {
                NoteTextField(
                    value =
                        "This is a longer note body that spans multiple lines and " +
                            "contains more detailed content about the topic.",
                    label = "Content",
                    isMultiline = true,
                    onValueChange = {},
                )
            }
        }
    }
}
