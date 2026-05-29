package com.example.android.playground.graphql.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.graphql.presentation.model.RepoUiModel
import com.example.android.playground.graphql.presentation.state.GitHubExplorerState
import org.junit.Rule
import org.junit.Test

class GitHubExplorerContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun emptyState() {
        paparazzi.snapshot {
            AppTheme {
                GitHubExplorerContent(
                    state = GitHubExplorerState(),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun loadingState() {
        paparazzi.snapshot {
            AppTheme {
                GitHubExplorerContent(
                    state = GitHubExplorerState(isLoading = true, searchQuery = "kotlin"),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun withResults() {
        paparazzi.snapshot {
            AppTheme {
                GitHubExplorerContent(
                    state =
                        GitHubExplorerState(
                            searchQuery = "android",
                            repos =
                                listOf(
                                    RepoUiModel(
                                        name = "android",
                                        nameWithOwner = "google/android",
                                        description = "Open-source project to improve Android developer tooling",
                                        starsDisplay = "8.2k",
                                        language = "Kotlin",
                                        languageColor = "#A97BFF",
                                        url = "https://github.com/google/android",
                                        ownerLogin = "google",
                                    ),
                                    RepoUiModel(
                                        name = "compose-samples",
                                        nameWithOwner = "android/compose-samples",
                                        description = "Official Jetpack Compose samples",
                                        starsDisplay = "19.4k",
                                        language = "Kotlin",
                                        languageColor = "#A97BFF",
                                        url = "https://github.com/android/compose-samples",
                                        ownerLogin = "android",
                                    ),
                                ),
                            totalCount = 2,
                        ),
                    onIntent = {},
                )
            }
        }
    }

    @Test
    fun errorState() {
        paparazzi.snapshot {
            AppTheme {
                GitHubExplorerContent(
                    state = GitHubExplorerState(error = "Network error. Please check your connection."),
                    onIntent = {},
                )
            }
        }
    }
}
