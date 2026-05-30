package com.example.android.playground.graphql.presentation.viewmodel

import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.graphql.domain.model.Repo
import com.example.android.playground.graphql.domain.model.RepoSearchResult
import com.example.android.playground.graphql.domain.usecase.ApolloSearchReposUseCase
import com.example.android.playground.graphql.domain.usecase.GetSavedTokenUseCase
import com.example.android.playground.graphql.domain.usecase.RawSearchReposUseCase
import com.example.android.playground.graphql.domain.usecase.SaveTokenUseCase
import com.example.android.playground.graphql.presentation.intent.GitHubExplorerIntent
import com.example.android.playground.graphql.presentation.model.DataSourceMode
import com.example.android.playground.graphql.presentation.sideeffect.GitHubExplorerSideEffect
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class GitHubExplorerViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val rawSearchReposUseCase: RawSearchReposUseCase = mockk()
    private val apolloSearchReposUseCase: ApolloSearchReposUseCase = mockk()
    private val saveTokenUseCase: SaveTokenUseCase = mockk()
    private val getSavedTokenUseCase: GetSavedTokenUseCase = mockk()

    private val sampleRepo =
        Repo(
            name = "kotlin",
            nameWithOwner = "JetBrains/kotlin",
            description = "The Kotlin PL",
            stars = 50000,
            language = "Kotlin",
            languageColor = "#F18E33",
            url = "https://github.com/JetBrains/kotlin",
            ownerLogin = "JetBrains",
            updatedAt = "2024-01-01T00:00:00Z",
        )

    private fun createViewModel(): GitHubExplorerViewModel {
        every { getSavedTokenUseCase() } returns flowOf("")
        return GitHubExplorerViewModel(
            rawSearchReposUseCase,
            apolloSearchReposUseCase,
            saveTokenUseCase,
            getSavedTokenUseCase,
        )
    }

    @Test
    fun initialStateHasDefaultValues() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                assertTrue(state.repos.isEmpty())
                assertEquals(DataSourceMode.RAW_OKHTTP, state.mode)
                assertEquals("", state.token)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onTokenChangedUpdatesTokenInState() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(GitHubExplorerIntent.OnTokenChanged("my-token"))

            viewModel.state.test {
                assertEquals("my-token", awaitItem().token)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onModeChangedUpdatesModeAndClearsResults() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(GitHubExplorerIntent.OnModeChanged(DataSourceMode.APOLLO))

            viewModel.state.test {
                val state = awaitItem()
                assertEquals(DataSourceMode.APOLLO, state.mode)
                assertTrue(state.repos.isEmpty())
                assertFalse(state.hasNextPage)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onSearchQueryChangedUpdatesSearchQueryInState() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(GitHubExplorerIntent.OnSearchQueryChanged("android"))

            viewModel.state.test {
                assertEquals("android", awaitItem().searchQuery)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onSaveTokenWithBlankTokenSetsError() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(GitHubExplorerIntent.OnSaveToken)

            viewModel.state.test {
                assertEquals("Token cannot be empty.", awaitItem().error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onSaveTokenWithValidTokenCallsSaveUseCaseAndEmitsShowMessage() =
        runTest {
            coEvery { saveTokenUseCase(any()) } just runs
            val viewModel = createViewModel()
            viewModel.handleIntent(GitHubExplorerIntent.OnTokenChanged("valid-token"))

            viewModel.handleIntent(GitHubExplorerIntent.OnSaveToken)

            viewModel.sideEffect.test {
                assertTrue(awaitItem() is GitHubExplorerSideEffect.ShowMessage)
                cancelAndIgnoreRemainingEvents()
            }
            coVerify(exactly = 1) { saveTokenUseCase("valid-token") }
        }

    @Test
    fun onDismissErrorClearsErrorInState() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(GitHubExplorerIntent.OnSaveToken)

            viewModel.handleIntent(GitHubExplorerIntent.OnDismissError)

            viewModel.state.test {
                assertNull(awaitItem().error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun navigateBackEmitsNavigateBackSideEffect() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(GitHubExplorerIntent.NavigateBack)

            viewModel.sideEffect.test {
                assertEquals(GitHubExplorerSideEffect.NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onRepoClickedEmitsOpenUrlSideEffectWithCorrectUrl() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(GitHubExplorerIntent.OnRepoClicked("https://github.com/test"))

            viewModel.sideEffect.test {
                val effect = awaitItem() as GitHubExplorerSideEffect.OpenUrl
                assertEquals("https://github.com/test", effect.url)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onSearchWithBlankQueryDoesNotStartLoading() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(GitHubExplorerIntent.OnSearch)

            viewModel.state.test {
                assertFalse(awaitItem().isLoading)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onSearchWithoutSavedTokenSetsError() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(GitHubExplorerIntent.OnSearchQueryChanged("kotlin"))

            viewModel.handleIntent(GitHubExplorerIntent.OnSearch)

            viewModel.state.test {
                assertEquals("Please save a GitHub PAT token first.", awaitItem().error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onSearchWithSavedTokenPopulatesReposInState() =
        runTest {
            every { getSavedTokenUseCase() } returns flowOf("saved-token")
            coEvery { rawSearchReposUseCase(any(), any(), any()) } returns
                RepoSearchResult(listOf(sampleRepo), 1, false, null)
            val viewModel =
                GitHubExplorerViewModel(
                    rawSearchReposUseCase,
                    apolloSearchReposUseCase,
                    saveTokenUseCase,
                    getSavedTokenUseCase,
                )
            viewModel.handleIntent(GitHubExplorerIntent.OnSearchQueryChanged("kotlin"))

            viewModel.handleIntent(GitHubExplorerIntent.OnSearch)

            viewModel.state.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                assertEquals(1, state.repos.size)
                assertEquals("kotlin", state.repos[0].name)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onSearchFailureSetsErrorMessageInState() =
        runTest {
            every { getSavedTokenUseCase() } returns flowOf("saved-token")
            coEvery { rawSearchReposUseCase(any(), any(), any()) } throws RuntimeException("Network error")
            val viewModel =
                GitHubExplorerViewModel(
                    rawSearchReposUseCase,
                    apolloSearchReposUseCase,
                    saveTokenUseCase,
                    getSavedTokenUseCase,
                )
            viewModel.handleIntent(GitHubExplorerIntent.OnSearchQueryChanged("kotlin"))

            viewModel.handleIntent(GitHubExplorerIntent.OnSearch)

            viewModel.state.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                assertEquals("Network error", state.error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onLoadMoreWhenHasNextPageIsFalseDoesNothing() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(GitHubExplorerIntent.OnLoadMore)

            viewModel.state.test {
                assertFalse(awaitItem().isLoadingMore)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onClearTokenCallsSaveTokenUseCaseWithEmptyString() =
        runTest {
            coEvery { saveTokenUseCase(any()) } just runs
            val viewModel = createViewModel()

            viewModel.handleIntent(GitHubExplorerIntent.OnClearToken)

            coVerify { saveTokenUseCase("") }
        }
}
