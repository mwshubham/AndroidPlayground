package com.example.android.playground.tictactoe.presentation.viewmodel

import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.tictactoe.api.TicTacToeGameRoute
import com.example.android.playground.tictactoe.domain.model.GameMode
import com.example.android.playground.tictactoe.domain.model.GameStatus
import com.example.android.playground.tictactoe.domain.usecase.GetAiMoveUseCase
import com.example.android.playground.tictactoe.domain.usecase.MakeMoveUseCase
import com.example.android.playground.tictactoe.domain.usecase.ResetGameUseCase
import com.example.android.playground.tictactoe.domain.usecase.SaveGameResultUseCase
import com.example.android.playground.tictactoe.domain.usecase.StartGameUseCase
import com.example.android.playground.tictactoe.presentation.intent.TicTacToeGameIntent
import com.example.android.playground.tictactoe.presentation.sideeffect.TicTacToeGameSideEffect
import com.example.android.playground.tictactoe.util.TicTacToeConstants
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test

class TicTacToeGameViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val startGameUseCase = StartGameUseCase()
    private val makeMoveUseCase = MakeMoveUseCase()
    private val resetGameUseCase = ResetGameUseCase()
    private val getAiMoveUseCase: GetAiMoveUseCase = mockk()
    private val saveGameResultUseCase: SaveGameResultUseCase = mockk()

    private val pvpRoute =
        TicTacToeGameRoute(
            mode = TicTacToeConstants.MODE_PVP,
            player1Name = "Alice",
            player2Name = "Bob",
        )

    private fun createViewModel() =
        TicTacToeGameViewModel(
            startGameUseCase,
            makeMoveUseCase,
            getAiMoveUseCase,
            resetGameUseCase,
            saveGameResultUseCase,
        )

    @Test
    fun `initGame sets up game with correct player names`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.initGame(pvpRoute)

            viewModel.state.test {
                val state = awaitItem()
                assertEquals("Alice", state.game.playerX.name)
                assertEquals("Bob", state.game.playerO.name)
                assertEquals(GameMode.PlayerVsPlayer, state.game.mode)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnCellTapped places a move on the board`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.initGame(pvpRoute)

            viewModel.handleIntent(TicTacToeGameIntent.OnCellTapped(0, 0))

            viewModel.state.test {
                val state = awaitItem()
                assertNotEquals(
                    com.example.android.playground.tictactoe.domain.model.Cell.Empty,
                    state.board.cells[0][0],
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnNavigateBack emits NavigateBack side effect`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.initGame(pvpRoute)

            viewModel.handleIntent(TicTacToeGameIntent.OnNavigateBack)

            viewModel.sideEffect.test {
                assertEquals(TicTacToeGameSideEffect.NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnResetGameTapped resets board to empty`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.initGame(pvpRoute)
            viewModel.handleIntent(TicTacToeGameIntent.OnCellTapped(0, 0))

            viewModel.handleIntent(TicTacToeGameIntent.OnResetGameTapped)

            viewModel.state.test {
                val state = awaitItem()
                assertEquals(9, state.board.emptyCells().size)
                assertEquals(GameStatus.InProgress, state.status)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `winning move sets status to Won and saves result`() =
        runTest {
            coEvery { saveGameResultUseCase(any()) } just runs
            val viewModel = createViewModel()
            viewModel.initGame(pvpRoute)

            // X plays (0,0),(0,1),(0,2) to win — O plays (1,0),(1,1) in between
            viewModel.handleIntent(TicTacToeGameIntent.OnCellTapped(0, 0)) // X
            viewModel.handleIntent(TicTacToeGameIntent.OnCellTapped(1, 0)) // O
            viewModel.handleIntent(TicTacToeGameIntent.OnCellTapped(0, 1)) // X
            viewModel.handleIntent(TicTacToeGameIntent.OnCellTapped(1, 1)) // O
            viewModel.handleIntent(TicTacToeGameIntent.OnCellTapped(0, 2)) // X wins

            viewModel.state.test {
                val state = awaitItem()
                assertEquals(GameStatus.Won::class, state.status::class)
                cancelAndIgnoreRemainingEvents()
            }
        }
}
