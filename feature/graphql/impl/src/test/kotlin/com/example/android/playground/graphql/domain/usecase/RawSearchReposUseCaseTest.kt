package com.example.android.playground.graphql.domain.usecase

import com.example.android.playground.graphql.domain.model.Repo
import com.example.android.playground.graphql.domain.model.RepoSearchResult
import com.example.android.playground.graphql.domain.repository.GitHubRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RawSearchReposUseCaseTest {
    private val repository: GitHubRepository = mockk()
    private lateinit var useCase: RawSearchReposUseCase

    private val sampleRepo =
        Repo(
            name = "kotlin",
            nameWithOwner = "JetBrains/kotlin",
            description = "The Kotlin Programming Language",
            stars = 50000,
            language = "Kotlin",
            languageColor = "#F18E33",
            url = "https://github.com/JetBrains/kotlin",
            ownerLogin = "JetBrains",
            updatedAt = "2024-01-01T00:00:00Z",
        )

    @Before
    fun setUp() {
        useCase = RawSearchReposUseCase(repository)
    }

    @Test
    fun invokeReturnsSearchResultFromRepository() =
        runTest {
            val expected =
                RepoSearchResult(
                    repos = listOf(sampleRepo),
                    totalCount = 1,
                    hasNextPage = false,
                    endCursor = null,
                )
            coEvery { repository.searchRepos(any(), any(), any()) } returns expected

            val result = useCase(query = "kotlin", token = "token123")

            assertEquals(expected, result)
        }

    @Test
    fun invokePassesQueryTokenAndCursorToRepository() =
        runTest {
            val expected = RepoSearchResult(emptyList(), 0, false, null)
            coEvery { repository.searchRepos(any(), any(), any()) } returns expected

            useCase(query = "android", token = "tok", after = "cursor_abc")

            coVerify(exactly = 1) { repository.searchRepos("android", "tok", "cursor_abc") }
        }

    @Test
    fun invokePassesNullCursorWhenNotProvided() =
        runTest {
            coEvery { repository.searchRepos(any(), any(), null) } returns
                RepoSearchResult(emptyList(), 0, false, null)

            useCase(query = "kotlin", token = "tok")

            coVerify(exactly = 1) { repository.searchRepos("kotlin", "tok", null) }
        }
}
