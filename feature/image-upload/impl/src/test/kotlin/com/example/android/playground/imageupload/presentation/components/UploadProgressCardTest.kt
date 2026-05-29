package com.example.android.playground.imageupload.presentation.components

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.imageupload.presentation.ImageUploadState
import org.junit.Rule
import org.junit.Test

class UploadProgressCardTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun uploading() {
        paparazzi.snapshot {
            AppTheme {
                UploadProgressCard(
                    state =
                        ImageUploadState(
                            isUploading = true,
                            successCount = 40,
                            failureCount = 10,
                        ),
                )
            }
        }
    }

    @Test
    fun idle() {
        paparazzi.snapshot {
            AppTheme {
                UploadProgressCard(state = ImageUploadState())
            }
        }
    }
}
