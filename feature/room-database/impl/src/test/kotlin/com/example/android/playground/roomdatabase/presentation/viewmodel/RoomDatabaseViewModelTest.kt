package com.example.android.playground.roomdatabase.presentation.viewmodel

import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.roomdatabase.domain.model.Author
import com.example.android.playground.roomdatabase.domain.model.AuthorWithBooks
import com.example.android.playground.roomdatabase.domain.usecase.GetAuthorsWithBooksUseCase
import com.example.android.playground.roomdatabase.domain.usecase.GetBooksWithTagsUseCase
import com.example.android.playground.roomdatabase.domain.usecase.SeedLibraryDataUseCase
import com.example.android.playground.roomdatabase.presentation.intent.RoomDatabaseIntent
import com.example.android.playground.roomdatabase.presentation.model.RoomDatabaseTab
import com.example.android.playground.roomdatabase.presentation.sideeffect.RoomDatabaseSideEffect
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class RoomDatabaseViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getAuthorsWithBooks: GetAuthorsWithBooksUseCase = mockk()
    private val getBooksWithTags: GetBooksWithTagsUseCase = mockk()
    private val seedLibraryData: SeedLibraryDataUseCase = mockk()

    private val sampleAuthor =
        Author(
            id = 1L,
            name = "Author A",
            email = "a@test.com",
            website = "https://test.com",
        )
    private val sampleAuthorWithBooks = AuthorWithBooks(author = sampleAuthor, books = emptyList())

    private fun createViewModel(): RoomDatabaseViewModel {
        coEvery { seedLibraryData() } just runs
        every { getAuthorsWithBooks() } returns flowOf(listOf(sampleAuthorWithBooks))
        every { getBooksWithTags() } returns flowOf(emptyList())
        return RoomDatabaseViewModel(getAuthorsWithBooks, getBooksWithTags, seedLibraryData)
    }

    @Test
    fun `init loads data and populates authors`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                assertEquals(1, state.authorsWithBooks.size)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnTabSelected updates selectedTab`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(RoomDatabaseIntent.OnTabSelected(RoomDatabaseTab.BOOKS_WITH_TAGS))

            viewModel.state.test {
                assertEquals(RoomDatabaseTab.BOOKS_WITH_TAGS, awaitItem().selectedTab)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `NavigateBack emits NavigateBack side effect`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(RoomDatabaseIntent.NavigateBack)

            viewModel.sideEffect.test {
                assertEquals(RoomDatabaseSideEffect.NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `seedLibraryData failure sets error in state`() =
        runTest {
            coEvery { seedLibraryData() } throws RuntimeException("DB error")
            every { getAuthorsWithBooks() } returns flowOf(emptyList())
            every { getBooksWithTags() } returns flowOf(emptyList())
            val viewModel = RoomDatabaseViewModel(getAuthorsWithBooks, getBooksWithTags, seedLibraryData)

            viewModel.state.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                assertNotNull(state.error)
                cancelAndIgnoreRemainingEvents()
            }
        }
}
