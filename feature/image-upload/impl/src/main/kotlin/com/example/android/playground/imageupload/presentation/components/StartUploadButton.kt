package com.example.android.playground.imageupload.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.imageupload.presentation.ImageUploadState

@Composable
fun StartUploadButton(
    modifier: Modifier = Modifier,
    state: ImageUploadState,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = !state.isUploading,
    ) {
        Icon(
            imageVector = Icons.Default.CloudUpload,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Start Upload")
    }
}

@ComponentPreview
@Composable
fun StartUploadButtonPreview() {
    PreviewContainer {
        StartUploadButton(
            state = ImageUploadState(),
            onClick = {},
        )
    }
}
