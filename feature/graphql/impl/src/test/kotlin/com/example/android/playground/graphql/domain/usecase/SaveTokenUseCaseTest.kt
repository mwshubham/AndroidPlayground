package com.example.android.playground.graphql.domain.usecase

import com.example.android.playground.graphql.domain.repository.GitHubRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveTokenUseCaseTest {
    private val repository: GitHubRepository = mockk()
    private lateinit var useCase: SaveTokenUseCase

    @Before
    fun setUp() {
        useCase = SaveTokenUseCase(repository)
    }

    @Test
    fun `invoke delegates token to repository`() =
        runTest {
            coEvery { repository.saveToken(any()) } just runs

            useCase("gh_myToken")

            coVerify(exactly = 1) { repository.saveToken("gh_myToken") }
        }

    @Test
    fun `invoke delegates empty token to repository for clearing`() =
        runTest {
            coEvery { repository.saveToken(any()) } just runs

            useCase("")

            coVerify(exactly = 1) { repository.saveToken("") }
        }
}
