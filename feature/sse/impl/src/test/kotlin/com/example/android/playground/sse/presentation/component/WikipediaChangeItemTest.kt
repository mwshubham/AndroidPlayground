package com.example.android.playground.sse.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.sse.presentation.state.WikipediaChangeUiModel
import org.junit.Rule
import org.junit.Test

class WikipediaChangeItemTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun editChange() {
        paparazzi.snapshot {
            AppTheme {
                WikipediaChangeItem(
                    change =
                        WikipediaChangeUiModel(
                            title = "Kotlin (programming language)",
                            user = "WikiEditor42",
                            wiki = "enwiki",
                            type = "edit",
                            comment = "Updated JVM interoperability section with new examples.",
                            formattedTime = "14:32",
                        ),
                )
            }
        }
    }

    @Test
    fun newArticleChange() {
        paparazzi.snapshot {
            AppTheme {
                WikipediaChangeItem(
                    change =
                        WikipediaChangeUiModel(
                            title = "Jetpack Compose 2026",
                            user = "NewArticleBot",
                            wiki = "enwiki",
                            type = "new",
                            comment = "Created article about the 2026 Compose release.",
                            formattedTime = "09:15",
                        ),
                )
            }
        }
    }
}
