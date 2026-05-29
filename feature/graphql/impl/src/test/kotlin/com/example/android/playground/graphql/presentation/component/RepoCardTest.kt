package com.example.android.playground.graphql.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.graphql.presentation.model.RepoUiModel
import org.junit.Rule
import org.junit.Test

class RepoCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun withLanguage() {
        paparazzi.snapshot {
            AppTheme {
                RepoCard(
                    repo =
                        RepoUiModel(
                            name = "compose-samples",
                            nameWithOwner = "android/compose-samples",
                            description = "Official Jetpack Compose samples from Google",
                            starsDisplay = "19.4k",
                            language = "Kotlin",
                            languageColor = "#A97BFF",
                            url = "https://github.com/android/compose-samples",
                            ownerLogin = "android",
                        ),
                    onClick = {},
                )
            }
        }
    }

    @Test
    fun withoutLanguage() {
        paparazzi.snapshot {
            AppTheme {
                RepoCard(
                    repo =
                        RepoUiModel(
                            name = "some-repo",
                            nameWithOwner = "user/some-repo",
                            description = "A repository without a detected language",
                            starsDisplay = "42",
                            language = null,
                            languageColor = null,
                            url = "https://github.com/user/some-repo",
                            ownerLogin = "user",
                        ),
                    onClick = {},
                )
            }
        }
    }

    @Test
    fun withoutDescription() {
        paparazzi.snapshot {
            AppTheme {
                RepoCard(
                    repo =
                        RepoUiModel(
                            name = "minimal-repo",
                            nameWithOwner = "user/minimal-repo",
                            description = "",
                            starsDisplay = "0",
                            language = "Java",
                            languageColor = "#B07219",
                            url = "https://github.com/user/minimal-repo",
                            ownerLogin = "user",
                        ),
                    onClick = {},
                )
            }
        }
    }
}
