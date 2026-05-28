package com.example.android.playground.websocket.presentation.viewmodel

import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.websocket.domain.model.WebSocketEvent
import com.example.android.playground.websocket.domain.usecase.ObserveBtcTickerUseCase
import com.example.android.playground.websocket.presentation.intent.WebSocketIntent
import com.example.android.playground.websocket.presentation.state.WebSocketConnectionStatus
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class WebSocketViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeBtcTickerUseCase: ObserveBtcTickerUseCase = mockk()

    private fun createViewModel(): WebSocketViewModel {
        every { observeBtcTickerUseCase() } returns flowOf()
        return WebSocketViewModel(observeBtcTickerUseCase)
    }

    @Test
    fun `initial state has Disconnected status`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                val state = awaitItem()
                assertEquals(WebSocketConnectionStatus.Disconnected, state.connectionStatus)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Disconnect intent sets status to Disconnected`() =
        runTest {
            every { observeBtcTickerUseCase() } returns flowOf(WebSocketEvent.Connecting)
            val viewModel = WebSocketViewModel(observeBtcTickerUseCase)

            viewModel.processIntent(WebSocketIntent.Connect)
            viewModel.processIntent(WebSocketIntent.Disconnect)

            viewModel.state.test {
                val state = awaitItem()
                assertEquals(WebSocketConnectionStatus.Disconnected, state.connectionStatus)
                cancelAndIgnoreRemainingEvents()
            }
        }
}
