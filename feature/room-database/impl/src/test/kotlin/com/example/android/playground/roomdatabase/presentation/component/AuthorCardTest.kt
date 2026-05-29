package com.example.android.playground.roomdatabase.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.roomdatabase.presentation.model.AuthorWithBooksUiModel
import org.junit.Rule
import org.junit.Test

class AuthorCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun withBooks() {
        paparazzi.snapshot {
            AppTheme {
                AuthorCard(
                    author =
                        AuthorWithBooksUiModel(
                            authorName = "Douglas Adams",
                            email = "dna@example.com",
                            website = "https://douglasadams.com",
                            books = listOf("The Hitchhiker's Guide", "The Restaurant at the End of the Universe"),
                        ),
                )
            }
        }
    }

    @Test
    fun noBooks() {
        paparazzi.snapshot {
            AppTheme {
                AuthorCard(
                    author =
                        AuthorWithBooksUiModel(
                            authorName = "New Author",
                            email = "author@example.com",
                            website = "",
                            books = emptyList(),
                        ),
                )
            }
        }
    }
}
