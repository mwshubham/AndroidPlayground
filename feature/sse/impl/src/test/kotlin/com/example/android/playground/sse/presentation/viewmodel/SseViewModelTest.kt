package com.example.android.playground.sse.presentation.viewmodel

import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.sse.domain.usecase.ObserveWikipediaChangesUseCase
import com.example.android.playground.sse.presentation.intent.SseIntent
import com.example.android.playground.sse.presentation.state.SseConnectionStatus
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SseViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeWikipediaChangesUseCase: ObserveWikipediaChangesUseCase = mockk()

    private fun createViewModel(): SseViewModel {
        every { observeWikipediaChangesUseCase() } returns flowOf()
        return SseViewModel(observeWikipediaChangesUseCase)
    }

    @Test
    fun `initial state is Disconnected with no changes`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                val state = awaitItem()
                assertEquals(SseConnectionStatus.Disconnected, state.connectionStatus)
                assertTrue(state.changes.isEmpty())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Clear intent empties the change list`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.processIntent(SseIntent.Clear)

            viewModel.state.test {
                assertTrue(awaitItem().changes.isEmpty())
                cancelAndIgnoreRemainingEvents()
            }
        }
}
