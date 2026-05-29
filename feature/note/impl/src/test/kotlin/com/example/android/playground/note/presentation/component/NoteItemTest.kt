package com.example.android.playground.note.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.note.presentation.model.NoteListItemUiModel
import org.junit.Rule
import org.junit.Test

class NoteItemTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun defaultItem() {
        paparazzi.snapshot {
            AppTheme {
                NoteItem(
                    note =
                        NoteListItemUiModel(
                            id = 1L,
                            title = "Shopping List",
                            content = "Milk, eggs, bread, butter",
                            updatedAtFormatted = "2 hours ago",
                        ),
                    onNoteClick = {},
                    onDeleteClick = {},
                )
            }
        }
    }

    @Test
    fun longContent() {
        paparazzi.snapshot {
            AppTheme {
                NoteItem(
                    note =
                        NoteListItemUiModel(
                            id = 2L,
                            title = "Meeting Notes — Q2 Planning",
                            content = "Discussed roadmap priorities, resource allocation for the next quarter, and identified blockers that need to be resolved before the end of the month.",
                            updatedAtFormatted = "Yesterday",
                        ),
                    onNoteClick = {},
                    onDeleteClick = {},
                )
            }
        }
    }

    @Test
    fun emptyContent() {
        paparazzi.snapshot {
            AppTheme {
                NoteItem(
                    note =
                        NoteListItemUiModel(
                            id = 3L,
                            title = "Empty Note",
                            content = "",
                            updatedAtFormatted = "Just now",
                        ),
                    onNoteClick = {},
                    onDeleteClick = {},
                )
            }
        }
    }
}
