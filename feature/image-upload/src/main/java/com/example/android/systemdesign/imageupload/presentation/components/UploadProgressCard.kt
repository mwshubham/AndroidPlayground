package com.example.android.systemdesign.imageupload.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.android.systemdesign.core.ui.preview.ComponentPreview
import com.example.android.systemdesign.core.ui.preview.PreviewContainer
import com.example.android.systemdesign.imageupload.domain.model.ImageUploadResult
import com.example.android.systemdesign.imageupload.domain.model.UploadStatus
import com.example.android.systemdesign.imageupload.presentation.ImageUploadState

@Composable
fun UploadProgressCard(state: ImageUploadState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Upload Progress",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { state.progress },
                modifier = Modifier.fillMaxWidth(),
            ) {
                // No Stop Indicator
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${state.uploadResults.size}/${state.totalCount} images processed",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (state.isUploading) {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Uploading...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@ComponentPreview
@Composable
fun UploadProgressCardPreview() {
    PreviewContainer {
        UploadProgressCard(
            ImageUploadState(
                isUploading = true,
                uploadResults = mutableListOf(
                    ImageUploadResult(
                        id = "image_1",
                        url = "https://example.com/images/image_1.jpg",
                        status = UploadStatus.SUCCESS
                    ),
                    ImageUploadResult(
                        id = "image_2",
                        url = "https://example.com/images/image_2.jpg",
                        status = UploadStatus.SUCCESS
                    ),
                    ImageUploadResult(
                        id = "image_3",
                        url = "https://example.com/images/image_3.jpg",
                        status = UploadStatus.FAILURE
                    )
                ),
                successCount = 2,
                failureCount = 1,
                totalCount = 10,
                progress = 0.3f
            )
        )
    }
}