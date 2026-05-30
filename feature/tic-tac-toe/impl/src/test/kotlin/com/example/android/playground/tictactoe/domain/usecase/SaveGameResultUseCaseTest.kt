package com.example.android.playground.tictactoe.domain.usecase

import com.example.android.playground.tictactoe.domain.model.GameResult
import com.example.android.playground.tictactoe.domain.repository.TicTacToeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SaveGameResultUseCaseTest {
    private val repository: TicTacToeRepository = mockk()
    private lateinit var useCase: SaveGameResultUseCase

    @Before
    fun setUp() {
        useCase = SaveGameResultUseCase(repository)
    }

    @Test
    fun `invoke delegates result to repository`() =
        runTest {
            val result = GameResult(winnerName = "Alice", mode = "PVP", totalMoves = 5, playedAt = 1000L)
            val captured = slot<GameResult>()
            coEvery { repository.saveGameResult(capture(captured)) } just runs

            useCase(result)

            coVerify(exactly = 1) { repository.saveGameResult(result) }
            assertEquals("Alice", captured.captured.winnerName)
        }

    @Test
    fun `invoke delegates draw result with null winner`() =
        runTest {
            val result = GameResult(winnerName = null, mode = "PVP", totalMoves = 9, playedAt = 2000L)
            coEvery { repository.saveGameResult(any()) } just runs

            useCase(result)

            coVerify(exactly = 1) { repository.saveGameResult(result) }
        }
}
