package com.example.android.playground.feed.presentation

import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.feed.domain.model.Topic
import com.example.android.playground.feed.domain.model.TopicId
import com.example.android.playground.feed.domain.usecase.GetFeedTopicsUseCase
import com.example.android.playground.feed.presentation.intent.FeedIntent
import com.example.android.playground.feed.presentation.sideeffect.FeedSideEffect
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class FeedViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getFeedTopicsUseCase: GetFeedTopicsUseCase = mockk()

    private val sampleTopics =
        listOf(
            Topic(id = TopicId.NoteApp, titleRes = 1, descriptionRes = 2),
            Topic(id = TopicId.LoginScreen, titleRes = 3, descriptionRes = 4),
        )

    private fun createViewModel() = FeedViewModel(getFeedTopicsUseCase)

    @Test
    fun `init loads topics and updates state`() =
        runTest {
            coEvery { getFeedTopicsUseCase() } returns sampleTopics

            val viewModel = createViewModel()

            viewModel.state.test {
                val state = awaitItem()
                assertEquals(sampleTopics, state.topics)
                assertFalse(state.isLoading)
                assertNull(state.error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `init failure sets error state`() =
        runTest {
            val errorMessage = "Network unavailable"
            coEvery { getFeedTopicsUseCase() } throws RuntimeException(errorMessage)

            val viewModel = createViewModel()

            viewModel.state.test {
                val state = awaitItem()
                assertEquals(errorMessage, state.error)
                assertFalse(state.isLoading)
                assertTrue(state.topics.isEmpty())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `init failure emits ShowError side effect`() =
        runTest {
            val errorMessage = "Network unavailable"
            coEvery { getFeedTopicsUseCase() } throws RuntimeException(errorMessage)

            val viewModel = createViewModel()

            viewModel.sideEffect.test {
                assertEquals(FeedSideEffect.ShowError(errorMessage), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `RefreshTopics intent reloads topics and emits TopicsRefreshed`() =
        runTest {
            coEvery { getFeedTopicsUseCase() } returns sampleTopics

            val viewModel = createViewModel()
            viewModel.handleIntent(FeedIntent.RefreshTopics)

            viewModel.state.test {
                val state = awaitItem()
                assertEquals(sampleTopics, state.topics)
                assertFalse(state.isRefreshing)
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.sideEffect.test {
                assertEquals(FeedSideEffect.TopicsRefreshed, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `RefreshTopics failure emits ShowError side effect`() =
        runTest {
            coEvery { getFeedTopicsUseCase() } returns sampleTopics andThenAnswer { throw IllegalStateException("Refresh failed") }

            val viewModel = createViewModel()
            viewModel.handleIntent(FeedIntent.RefreshTopics)

            viewModel.sideEffect.test {
                val effect = awaitItem()
                assertTrue(effect is FeedSideEffect.ShowError)
                cancelAndIgnoreRemainingEvents()
            }
        }
}
