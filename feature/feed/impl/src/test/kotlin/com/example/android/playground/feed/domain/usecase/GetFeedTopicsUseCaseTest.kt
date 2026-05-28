package com.example.android.playground.feed.domain.usecase

import com.example.android.playground.feed.domain.model.Topic
import com.example.android.playground.feed.domain.model.TopicId
import com.example.android.playground.feed.domain.repository.FeedRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetFeedTopicsUseCaseTest {
    private val repository: FeedRepository = mockk()
    private lateinit var useCase: GetFeedTopicsUseCase

    private val sampleTopics =
        listOf(
            Topic(id = TopicId.NoteApp, titleRes = 1, descriptionRes = 2),
            Topic(id = TopicId.LoginScreen, titleRes = 3, descriptionRes = 4),
        )

    @Before
    fun setUp() {
        useCase = GetFeedTopicsUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository and returns its result`() =
        runTest {
            coEvery { repository.getTopics() } returns sampleTopics

            val result = useCase()

            assertEquals(sampleTopics, result)
            coVerify(exactly = 1) { repository.getTopics() }
        }

    @Test
    fun `invoke returns empty list when repository returns empty`() =
        runTest {
            coEvery { repository.getTopics() } returns emptyList()

            val result = useCase()

            assertTrue(result.isEmpty())
        }

    @Test
    fun `invoke propagates exception from repository`() =
        runTest {
            coEvery { repository.getTopics() } throws RuntimeException("Network error")

            val exception = runCatching { useCase() }.exceptionOrNull()

            assertTrue(exception is RuntimeException)
            assertEquals("Network error", exception?.message)
        }
}
