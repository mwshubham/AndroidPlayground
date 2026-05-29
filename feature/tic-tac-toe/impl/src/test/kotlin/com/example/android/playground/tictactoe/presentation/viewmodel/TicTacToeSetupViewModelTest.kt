package com.example.android.playground.tictactoe.presentation.viewmodel

import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.tictactoe.domain.model.GameMode
import com.example.android.playground.tictactoe.domain.model.GameResult
import com.example.android.playground.tictactoe.domain.usecase.GetGameHistoryUseCase
import com.example.android.playground.tictactoe.presentation.intent.TicTacToeSetupIntent
import com.example.android.playground.tictactoe.presentation.sideeffect.TicTacToeSetupSideEffect
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class TicTacToeSetupViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getGameHistoryUseCase: GetGameHistoryUseCase = mockk()

    private fun createViewModel(): TicTacToeSetupViewModel {
        every { getGameHistoryUseCase() } returns flowOf(emptyList())
        return TicTacToeSetupViewModel(getGameHistoryUseCase)
    }

    @Test
    fun `init observes game history and updates gamesPlayed`() =
        runTest {
            val history =
                listOf(
                    GameResult(winnerName = "Alice", mode = "PVP", totalMoves = 5, playedAt = 1000L),
                    GameResult(winnerName = null, mode = "PVP", totalMoves = 9, playedAt = 2000L),
                )
            every { getGameHistoryUseCase() } returns flowOf(history)
            val viewModel = TicTacToeSetupViewModel(getGameHistoryUseCase)

            viewModel.state.test {
                assertEquals(2, awaitItem().gamesPlayed)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnPlayer1NameChanged updates player1Name`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(TicTacToeSetupIntent.OnPlayer1NameChanged("Alice"))

            viewModel.state.test {
                assertEquals("Alice", awaitItem().player1Name)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnPlayer2NameChanged updates player2Name`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(TicTacToeSetupIntent.OnPlayer2NameChanged("Bob"))

            viewModel.state.test {
                assertEquals("Bob", awaitItem().player2Name)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `isStartEnabled is true when both player names are non-blank`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(TicTacToeSetupIntent.OnPlayer1NameChanged("Alice"))
            viewModel.handleIntent(TicTacToeSetupIntent.OnPlayer2NameChanged("Bob"))

            viewModel.state.test {
                assertTrue(awaitItem().isStartEnabled)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `isStartEnabled is false when player1Name is blank`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(TicTacToeSetupIntent.OnPlayer2NameChanged("Bob"))

            viewModel.state.test {
                assertFalse(awaitItem().isStartEnabled)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnModeSelected PlayerVsAi sets AI as player2 name and updates mode`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(TicTacToeSetupIntent.OnModeSelected(GameMode.PlayerVsAi))

            viewModel.state.test {
                val state = awaitItem()
                assertEquals(GameMode.PlayerVsAi, state.selectedMode)
                assertTrue(state.player2Name.isNotBlank())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnStartGameTapped when enabled emits NavigateToGame side effect`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(TicTacToeSetupIntent.OnPlayer1NameChanged("Alice"))
            viewModel.handleIntent(TicTacToeSetupIntent.OnPlayer2NameChanged("Bob"))

            viewModel.handleIntent(TicTacToeSetupIntent.OnStartGameTapped)

            viewModel.sideEffect.test {
                assertTrue(awaitItem() is TicTacToeSetupSideEffect.NavigateToGame)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnStartGameTapped when not enabled does not emit side effect`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(TicTacToeSetupIntent.OnStartGameTapped)

            viewModel.sideEffect.test {
                expectNoEvents()
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnNavigateBack emits NavigateBack side effect`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(TicTacToeSetupIntent.OnNavigateBack)

            viewModel.sideEffect.test {
                assertEquals(TicTacToeSetupSideEffect.NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
}
