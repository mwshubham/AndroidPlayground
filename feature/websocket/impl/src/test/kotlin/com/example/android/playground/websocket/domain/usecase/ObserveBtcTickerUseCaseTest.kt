package com.example.android.playground.websocket.domain.usecase

import com.example.android.playground.websocket.domain.model.WebSocketEvent
import com.example.android.playground.websocket.domain.repository.WebSocketRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ObserveBtcTickerUseCaseTest {
    private val repository: WebSocketRepository = mockk()
    private lateinit var useCase: ObserveBtcTickerUseCase

    @Before
    fun setUp() {
        useCase = ObserveBtcTickerUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository observeTicker`() =
        runTest {
            every { repository.observeTicker() } returns flowOf(WebSocketEvent.Connected)

            val result = useCase().toList()

            assertEquals(listOf(WebSocketEvent.Connected), result)
            verify(exactly = 1) { repository.observeTicker() }
        }
}
