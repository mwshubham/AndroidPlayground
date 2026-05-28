package com.example.android.playground.note.presentation.viewmodel

import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.note.domain.model.Note
import com.example.android.playground.note.domain.usecase.DeleteNoteUseCase
import com.example.android.playground.note.domain.usecase.GetNotesUseCase
import com.example.android.playground.note.presentation.intent.NoteListIntent
import com.example.android.playground.note.presentation.sideeffect.NoteListSideEffect
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class NoteListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getNotesUseCase: GetNotesUseCase = mockk()
    private val deleteNoteUseCase: DeleteNoteUseCase = mockk()

    private val note1 = Note(id = 1L, title = "Alpha", content = "First note", createdAt = 1000L, updatedAt = 1000L)
    private val note2 = Note(id = 2L, title = "Beta", content = "Second note", createdAt = 2000L, updatedAt = 2000L)

    private fun createViewModel(): NoteListViewModel {
        every { getNotesUseCase() } returns flowOf(listOf(note1, note2))
        return NoteListViewModel(getNotesUseCase, deleteNoteUseCase, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun `init loads notes and sets state with isLoading false`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `init loads notes and populates notes list`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                val state = awaitItem()
                assertEquals(2, state.notes.size)
                assertEquals("Alpha", state.notes[0].title)
                assertEquals("Beta", state.notes[1].title)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `SearchNotes intent updates searchQuery in state`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(NoteListIntent.SearchNotes("Alpha"))

            viewModel.state.test {
                val state = awaitItem()
                assertEquals("Alpha", state.searchQuery)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `DeleteNote intent calls deleteNoteUseCase and emits ShowSuccessMessage`() =
        runTest {
            coEvery { deleteNoteUseCase(1L) } just runs
            val viewModel = createViewModel()

            viewModel.handleIntent(NoteListIntent.DeleteNote(1L))

            viewModel.sideEffect.test {
                val effect = awaitItem()
                assertTrue(effect is NoteListSideEffect.ShowSuccessMessage)
                cancelAndIgnoreRemainingEvents()
            }
            coVerify(exactly = 1) { deleteNoteUseCase(1L) }
        }

    @Test
    fun `DeleteNote failure emits ShowErrorMessage`() =
        runTest {
            coEvery { deleteNoteUseCase(any()) } throws RuntimeException("DB error")
            val viewModel = createViewModel()

            viewModel.handleIntent(NoteListIntent.DeleteNote(99L))

            viewModel.sideEffect.test {
                val effect = awaitItem()
                assertTrue(effect is NoteListSideEffect.ShowErrorMessage)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `NavigateToDetail intent emits NavigateToNoteDetail side effect`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(NoteListIntent.NavigateToDetail(1L))

            viewModel.sideEffect.test {
                assertEquals(NoteListSideEffect.NavigateToNoteDetail(1L), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `NavigateToAdd intent emits NavigateToAddNote side effect`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(NoteListIntent.NavigateToAdd)

            viewModel.sideEffect.test {
                assertEquals(NoteListSideEffect.NavigateToAddNote, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `NavigateBack intent emits NavigateBack side effect`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(NoteListIntent.NavigateBack)

            viewModel.sideEffect.test {
                assertEquals(NoteListSideEffect.NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
}
