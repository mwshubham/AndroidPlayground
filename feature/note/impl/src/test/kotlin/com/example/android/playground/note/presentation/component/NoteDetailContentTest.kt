package com.example.android.playground.note.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.note.presentation.model.NoteDetailUiModel
import com.example.android.playground.note.presentation.state.NoteDetailState
import org.junit.Rule
import org.junit.Test

class NoteDetailContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    private val sampleNote =
        NoteDetailUiModel(
            id = 1L,
            title = "Meeting Notes",
            content =
                "Discussed Q3 roadmap and team allocations. " +
                    "Next sprint starts Monday with 8 story points assigned.",
            createdAtFormatted = "May 20, 2026",
            updatedAtFormatted = "May 29, 2026",
        )

    @Test
    fun loadingState() {
        paparazzi.snapshot {
            AppTheme {
                NoteDetailContent(
                    state = NoteDetailState(isLoading = true),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun readMode() {
        paparazzi.snapshot {
            AppTheme {
                NoteDetailContent(
                    state = NoteDetailState(note = sampleNote),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun editMode() {
        paparazzi.snapshot {
            AppTheme {
                NoteDetailContent(
                    state =
                        NoteDetailState(
                            note = sampleNote,
                            isEditing = true,
                            title = sampleNote.title,
                            content = sampleNote.content,
                        ),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun newNoteMode() {
        paparazzi.snapshot {
            AppTheme {
                NoteDetailContent(
                    state = NoteDetailState(isEditing = true),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun errorState() {
        paparazzi.snapshot {
            AppTheme {
                NoteDetailContent(
                    state =
                        NoteDetailState(
                            note = sampleNote,
                            error = "Failed to save note.",
                        ),
                    onIntent = {},
                )
            }
        }
    }
}
