package com.example.android.playground.note.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.note.presentation.model.NoteListItemUiModel
import com.example.android.playground.note.presentation.state.NoteListState
import org.junit.Rule
import org.junit.Test

class NoteListContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun loadingState() {
        paparazzi.snapshot {
            AppTheme {
                NoteListContent(
                    state = NoteListState(isLoading = true),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun emptyState() {
        paparazzi.snapshot {
            AppTheme {
                NoteListContent(
                    state = NoteListState(notes = emptyList()),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun loadedWithNotes() {
        val notes =
            listOf(
                NoteListItemUiModel(
                    id = 1L,
                    title = "Meeting Notes",
                    content = "Discussed Q3 roadmap and team allocations for upcoming sprint.",
                    updatedAtFormatted = "2 hours ago",
                ),
                NoteListItemUiModel(
                    id = 2L,
                    title = "Shopping List",
                    content = "Milk, eggs, bread, coffee beans, olive oil.",
                    updatedAtFormatted = "Yesterday",
                ),
                NoteListItemUiModel(
                    id = 3L,
                    title = "Book Summary",
                    content = "Key takeaways from Clean Architecture by Robert C. Martin.",
                    updatedAtFormatted = "3 days ago",
                ),
            )
        paparazzi.snapshot {
            AppTheme {
                NoteListContent(
                    state = NoteListState(notes = notes),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun searchQueryActive() {
        val notes =
            listOf(
                NoteListItemUiModel(
                    id = 1L,
                    title = "Meeting Notes",
                    content = "Discussed Q3 roadmap.",
                    updatedAtFormatted = "2 hours ago",
                ),
            )
        paparazzi.snapshot {
            AppTheme {
                NoteListContent(
                    state = NoteListState(notes = notes, searchQuery = "meeting"),
                    onIntent = {},
                )
            }
        }
    }
}
