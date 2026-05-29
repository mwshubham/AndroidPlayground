package com.example.android.playground.mediaorchestrator.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.mediaorchestrator.domain.model.UploadStatus
import com.example.android.playground.mediaorchestrator.presentation.model.MediaItemUiModel
import org.junit.Rule
import org.junit.Test

class MediaItemCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun pendingItem() {
        paparazzi.snapshot {
            AppTheme {
                MediaItemCard(
                    item =
                        MediaItemUiModel(
                            id = "1",
                            name = "photo_001.jpg",
                            sizeDisplay = "2.4 MB",
                            status = UploadStatus.PENDING,
                            progress = 0f,
                            remoteUrl = null,
                        ),
                )
            }
        }
    }

    @Test
    fun inProgressItem() {
        paparazzi.snapshot {
            AppTheme {
                MediaItemCard(
                    item =
                        MediaItemUiModel(
                            id = "2",
                            name = "video_clip.mp4",
                            sizeDisplay = "45.1 MB",
                            status = UploadStatus.IN_PROGRESS,
                            progress = 0.6f,
                            remoteUrl = null,
                        ),
                )
            }
        }
    }

    @Test
    fun successItem() {
        paparazzi.snapshot {
            AppTheme {
                MediaItemCard(
                    item =
                        MediaItemUiModel(
                            id = "3",
                            name = "document.pdf",
                            sizeDisplay = "512 KB",
                            status = UploadStatus.SUCCESS,
                            progress = 1f,
                            remoteUrl = "https://cdn.example.com/document.pdf",
                        ),
                )
            }
        }
    }

    @Test
    fun failedItem() {
        paparazzi.snapshot {
            AppTheme {
                MediaItemCard(
                    item =
                        MediaItemUiModel(
                            id = "4",
                            name = "backup.zip",
                            sizeDisplay = "128 MB",
                            status = UploadStatus.FAILED,
                            progress = 0f,
                            remoteUrl = null,
                        ),
                )
            }
        }
    }
}
