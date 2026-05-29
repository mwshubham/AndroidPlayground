package com.example.android.playground.feed.presentation.components

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.feed.domain.model.Topic
import com.example.android.playground.feed.domain.model.TopicId
import org.junit.Rule
import org.junit.Test

class TopicCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun defaultCard() {
        paparazzi.snapshot {
            AppTheme {
                TopicCard(
                    topic =
                        Topic(
                            id = TopicId.NoteApp,
                            titleRes = android.R.string.ok,
                            descriptionRes = android.R.string.cancel,
                        ),
                    onClick = {},
                )
            }
        }
    }
}
