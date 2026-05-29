package com.example.android.playground.roomdatabase.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.roomdatabase.presentation.model.AuthorWithBooksUiModel
import com.example.android.playground.roomdatabase.presentation.model.BookWithTagsUiModel
import com.example.android.playground.roomdatabase.presentation.model.RoomDatabaseTab
import com.example.android.playground.roomdatabase.presentation.state.RoomDatabaseState
import org.junit.Rule
import org.junit.Test

class RoomDatabaseContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun emptyState() {
        paparazzi.snapshot {
            AppTheme {
                RoomDatabaseContent(
                    state = RoomDatabaseState(),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun loadingState() {
        paparazzi.snapshot {
            AppTheme {
                RoomDatabaseContent(
                    state = RoomDatabaseState(isLoading = true),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun authorsTab() {
        paparazzi.snapshot {
            AppTheme {
                RoomDatabaseContent(
                    state =
                        RoomDatabaseState(
                            selectedTab = RoomDatabaseTab.AUTHORS,
                            authorsWithBooks =
                                listOf(
                                    AuthorWithBooksUiModel(
                                        authorName = "Douglas Adams",
                                        email = "dna@example.com",
                                        website = "https://douglasadams.com",
                                        books = listOf("The Hitchhiker's Guide", "The Restaurant at the End of the Universe"),
                                    ),
                                ),
                        ),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun booksTab() {
        paparazzi.snapshot {
            AppTheme {
                RoomDatabaseContent(
                    state =
                        RoomDatabaseState(
                            selectedTab = RoomDatabaseTab.BOOKS_WITH_TAGS,
                            booksWithTags =
                                listOf(
                                    BookWithTagsUiModel(
                                        title = "The Hitchhiker's Guide to the Galaxy",
                                        publishYear = 1979,
                                        genres = "Science Fiction, Comedy",
                                        tags = listOf("classic", "humour", "sci-fi"),
                                    ),
                                ),
                        ),
                    onIntent = {},
                )
            }
        }
    }
}
