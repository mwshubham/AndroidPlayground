package com.example.android.playground.userinitiatedservice.presentation.component

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.userinitiatedservice.domain.model.TransferStatus
import com.example.android.playground.userinitiatedservice.presentation.model.TransferItemUiModel
import org.junit.Rule
import org.junit.Test

class TransferItemCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun pendingItem() {
        paparazzi.snapshot {
            AppTheme {
                TransferItemCard(
                    item =
                        TransferItemUiModel(
                            id = "1",
                            name = "document.pdf",
                            sizeDisplay = "1.2 MB",
                            status = TransferStatus.PENDING,
                            progress = 0f,
                        ),
                )
            }
        }
    }

    @Test
    fun inProgressItem() {
        paparazzi.snapshot {
            AppTheme {
                TransferItemCard(
                    item =
                        TransferItemUiModel(
                            id = "2",
                            name = "large_video.mp4",
                            sizeDisplay = "54.3 MB",
                            status = TransferStatus.RUNNING,
                            progress = 0.65f,
                        ),
                )
            }
        }
    }

    @Test
    fun completedItem() {
        paparazzi.snapshot {
            AppTheme {
                TransferItemCard(
                    item =
                        TransferItemUiModel(
                            id = "3",
                            name = "photo.jpg",
                            sizeDisplay = "3.8 MB",
                            status = TransferStatus.SUCCESS,
                            progress = 1f,
                        ),
                )
            }
        }
    }
}
