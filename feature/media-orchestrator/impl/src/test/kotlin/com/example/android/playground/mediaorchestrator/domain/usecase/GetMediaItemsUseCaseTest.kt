package com.example.android.playground.mediaorchestrator.domain.usecase

import com.example.android.playground.mediaorchestrator.domain.repository.MediaRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetMediaItemsUseCaseTest {
    private val repository: MediaRepository = mockk()
    private lateinit var useCase: GetMediaItemsUseCase

    @Before
    fun setUp() {
        useCase = GetMediaItemsUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository observeAll and returns its flow`() =
        runTest {
            every { repository.observeAll() } returns flowOf(emptyList())

            val result = useCase().toList()

            assertEquals(listOf(emptyList<Any>()), result)
            verify(exactly = 1) { repository.observeAll() }
        }

    @Test
    fun `invoke returns empty list when repository has no items`() =
        runTest {
            every { repository.observeAll() } returns flowOf(emptyList())

            val result = useCase().toList()

            assertTrue(result.first().isEmpty())
        }
}
