package com.example.android.playground.graphql.domain.usecase

import com.example.android.playground.graphql.domain.repository.GitHubRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetSavedTokenUseCaseTest {
    private val repository: GitHubRepository = mockk()
    private lateinit var useCase: GetSavedTokenUseCase

    @Before
    fun setUp() {
        useCase = GetSavedTokenUseCase(repository)
    }

    @Test
    fun `invoke returns flow from repository`() =
        runTest {
            every { repository.getToken() } returns flowOf("my-token")

            val result = useCase().toList()

            assertEquals(listOf("my-token"), result)
        }

    @Test
    fun `invoke returns empty string when no token saved`() =
        runTest {
            every { repository.getToken() } returns flowOf("")

            val result = useCase().toList()

            assertEquals(listOf(""), result)
        }

    @Test
    fun `invoke delegates to repository exactly once`() =
        runTest {
            every { repository.getToken() } returns flowOf("token")

            useCase()

            verify(exactly = 1) { repository.getToken() }
        }
}
