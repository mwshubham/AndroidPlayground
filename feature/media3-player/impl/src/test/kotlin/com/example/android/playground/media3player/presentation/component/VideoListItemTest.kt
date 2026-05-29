package com.example.android.playground.media3player.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.media3player.presentation.model.VideoUiModel
import org.junit.Rule
import org.junit.Test

class VideoListItemTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun unselected() {
        paparazzi.snapshot {
            AppTheme {
                VideoListItem(
                    video =
                        VideoUiModel(
                            id = "1",
                            title = "Big Buck Bunny",
                            dashUrl = "https://example.com/bbb.mpd",
                            drmLicenseUrl = null,
                            description = "A short animated film produced by the Blender Foundation",
                        ),
                    isSelected = false,
                    onClick = {},
                )
            }
        }
    }

    @Test
    fun selected() {
        paparazzi.snapshot {
            AppTheme {
                VideoListItem(
                    video =
                        VideoUiModel(
                            id = "1",
                            title = "Big Buck Bunny",
                            dashUrl = "https://example.com/bbb.mpd",
                            drmLicenseUrl = null,
                            description = "A short animated film produced by the Blender Foundation",
                        ),
                    isSelected = true,
                    onClick = {},
                )
            }
        }
    }
}
