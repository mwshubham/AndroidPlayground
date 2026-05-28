package com.example.android.playground.sse.domain.usecase

import com.example.android.playground.sse.domain.model.SseEvent
import com.example.android.playground.sse.domain.repository.SseRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ObserveWikipediaChangesUseCaseTest {
    private val repository: SseRepository = mockk()
    private lateinit var useCase: ObserveWikipediaChangesUseCase

    @Before
    fun setUp() {
        useCase = ObserveWikipediaChangesUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository observeChanges`() =
        runTest {
            every { repository.observeChanges() } returns flowOf()

            val result = useCase().toList()

            assertEquals(emptyList<SseEvent>(), result)
            verify(exactly = 1) { repository.observeChanges() }
        }
}
