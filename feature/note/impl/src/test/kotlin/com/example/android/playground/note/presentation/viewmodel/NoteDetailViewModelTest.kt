package com.example.android.playground.note.presentation.viewmodel

import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.note.domain.model.Note
import com.example.android.playground.note.domain.usecase.GetNoteByIdUseCase
import com.example.android.playground.note.domain.usecase.InsertNoteUseCase
import com.example.android.playground.note.domain.usecase.UpdateNoteContentUseCase
import com.example.android.playground.note.presentation.intent.NoteDetailIntent
import com.example.android.playground.note.presentation.sideeffect.NoteDetailSideEffect
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class NoteDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getNoteByIdUseCase: GetNoteByIdUseCase = mockk()
    private val insertNoteUseCase: InsertNoteUseCase = mockk()
    private val updateNoteContentUseCase: UpdateNoteContentUseCase = mockk()

    private val existingNote =
        Note(
            id = 1L,
            title = "Existing Title",
            content = "Existing Content",
            createdAt = 1000L,
            updatedAt = 2000L,
        )

    private fun createViewModelForNew() = NoteDetailViewModel(getNoteByIdUseCase, insertNoteUseCase, updateNoteContentUseCase, noteId = null)

    private fun createViewModelForEdit(noteId: Long) = NoteDetailViewModel(getNoteByIdUseCase, insertNoteUseCase, updateNoteContentUseCase, noteId = noteId)

    @Test
    fun `init with null noteId sets isEditing true and empty fields`() =
        runTest {
            val viewModel = createViewModelForNew()

            viewModel.state.test {
                val state = awaitItem()
                assertTrue(state.isEditing)
                assertNull(state.note)
                assertEquals("", state.title)
                assertEquals("", state.content)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `init with existing noteId loads note and populates state`() =
        runTest {
            coEvery { getNoteByIdUseCase(1L) } returns existingNote

            val viewModel = createViewModelForEdit(1L)

            viewModel.state.test {
                val state = awaitItem()
                assertNotNull(state.note)
                assertEquals("Existing Title", state.title)
                assertEquals("Existing Content", state.content)
                assertFalse(state.isLoading)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `init with non-existent noteId sets error state and emits ShowErrorMessage`() =
        runTest {
            coEvery { getNoteByIdUseCase(999L) } returns null

            val viewModel = createViewModelForEdit(999L)

            viewModel.state.test {
                val state = awaitItem()
                assertEquals("Note not found", state.error)
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.sideEffect.test {
                val effect = awaitItem()
                assertTrue(effect is NoteDetailSideEffect.ShowErrorMessage)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `UpdateTitle intent updates title in state`() =
        runTest {
            val viewModel = createViewModelForNew()

            viewModel.handleIntent(NoteDetailIntent.UpdateTitle("New Title"))

            viewModel.state.test {
                val state = awaitItem()
                assertEquals("New Title", state.title)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `UpdateContent intent updates content in state`() =
        runTest {
            val viewModel = createViewModelForNew()

            viewModel.handleIntent(NoteDetailIntent.UpdateContent("New Content"))

            viewModel.state.test {
                val state = awaitItem()
                assertEquals("New Content", state.content)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `SaveNote with empty title sets error and emits ShowErrorMessage`() =
        runTest {
            val viewModel = createViewModelForNew()

            viewModel.handleIntent(NoteDetailIntent.SaveNote)

            viewModel.state.test {
                val state = awaitItem()
                assertNotNull(state.error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `SaveNote for new note inserts and emits NavigateBack`() =
        runTest {
            coEvery { insertNoteUseCase(any(), any()) } returns 1L

            val viewModel = createViewModelForNew()
            viewModel.handleIntent(NoteDetailIntent.UpdateTitle("New Title"))
            viewModel.handleIntent(NoteDetailIntent.UpdateContent("New Content"))
            viewModel.handleIntent(NoteDetailIntent.SaveNote)

            viewModel.sideEffect.test {
                assertEquals(NoteDetailSideEffect.NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            coVerify(exactly = 1) { insertNoteUseCase("New Title", "New Content") }
        }

    @Test
    fun `EditNote intent sets isEditing to true`() =
        runTest {
            coEvery { getNoteByIdUseCase(1L) } returns existingNote

            val viewModel = createViewModelForEdit(1L)
            viewModel.handleIntent(NoteDetailIntent.EditNote)

            viewModel.state.test {
                val state = awaitItem()
                assertTrue(state.isEditing)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `NavigateBack intent emits NavigateBack side effect`() =
        runTest {
            val viewModel = createViewModelForNew()

            viewModel.handleIntent(NoteDetailIntent.NavigateBack)

            viewModel.sideEffect.test {
                assertEquals(NoteDetailSideEffect.NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `ClearError intent clears error in state`() =
        runTest {
            val viewModel = createViewModelForNew()
            // Trigger an error first
            viewModel.handleIntent(NoteDetailIntent.SaveNote)

            viewModel.handleIntent(NoteDetailIntent.ClearError)

            viewModel.state.test {
                val state = awaitItem()
                assertNull(state.error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `SaveNote for existing note calls updateNoteContentUseCase`() =
        runTest {
            coEvery { getNoteByIdUseCase(1L) } returns existingNote
            coEvery { updateNoteContentUseCase(any(), any(), any()) } just runs
            coEvery { getNoteByIdUseCase(1L) } returns existingNote.copy(title = "Updated Title")

            val viewModel = createViewModelForEdit(1L)
            viewModel.handleIntent(NoteDetailIntent.UpdateTitle("Updated Title"))
            viewModel.handleIntent(NoteDetailIntent.SaveNote)

            coVerify { updateNoteContentUseCase(1L, "Updated Title", any()) }
        }
}
