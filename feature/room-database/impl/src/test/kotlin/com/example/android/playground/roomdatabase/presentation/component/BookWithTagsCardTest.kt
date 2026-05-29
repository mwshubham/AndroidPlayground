package com.example.android.playground.roomdatabase.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.roomdatabase.presentation.model.BookWithTagsUiModel
import org.junit.Rule
import org.junit.Test

class BookWithTagsCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun withTags() {
        paparazzi.snapshot {
            AppTheme {
                BookWithTagsCard(
                    book =
                        BookWithTagsUiModel(
                            title = "The Hitchhiker's Guide to the Galaxy",
                            publishYear = 1979,
                            genres = "Science Fiction, Comedy",
                            tags = listOf("classic", "humour", "sci-fi"),
                        ),
                )
            }
        }
    }

    @Test
    fun noTags() {
        paparazzi.snapshot {
            AppTheme {
                BookWithTagsCard(
                    book =
                        BookWithTagsUiModel(
                            title = "Untitled Draft",
                            publishYear = 2024,
                            genres = "Unknown",
                            tags = emptyList(),
                        ),
                )
            }
        }
    }
}
