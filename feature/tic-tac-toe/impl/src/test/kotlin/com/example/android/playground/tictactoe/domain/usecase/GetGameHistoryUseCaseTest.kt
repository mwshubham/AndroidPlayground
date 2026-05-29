package com.example.android.playground.tictactoe.domain.usecase

import com.example.android.playground.tictactoe.domain.model.GameResult
import com.example.android.playground.tictactoe.domain.repository.TicTacToeRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetGameHistoryUseCaseTest {
    private val repository: TicTacToeRepository = mockk()
    private lateinit var useCase: GetGameHistoryUseCase

    private val result1 = GameResult(id = 1L, winnerName = "Alice", mode = "PVP", totalMoves = 5, playedAt = 1000L)
    private val result2 = GameResult(id = 2L, winnerName = null, mode = "PVP", totalMoves = 9, playedAt = 2000L)

    @Before
    fun setUp() {
        useCase = GetGameHistoryUseCase(repository)
    }

    @Test
    fun `invoke returns game history flow from repository`() =
        runTest {
            every { repository.getGameHistory() } returns flowOf(listOf(result1, result2))

            val history = useCase().toList().flatten()

            assertEquals(2, history.size)
            assertEquals(result1, history[0])
        }

    @Test
    fun `invoke returns empty flow when no history`() =
        runTest {
            every { repository.getGameHistory() } returns flowOf(emptyList())

            val history = useCase().toList().flatten()

            assertEquals(0, history.size)
        }

    @Test
    fun `invoke delegates to repository exactly once`() =
        runTest {
            every { repository.getGameHistory() } returns flowOf(emptyList())

            useCase()

            verify(exactly = 1) { repository.getGameHistory() }
        }
}
