package com.example.android.playground.imageupload.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer

@Composable
fun ClearResultButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Clear")
    }
}

@ComponentPreview
@Composable
fun ClearResultButtonPreview() {
    PreviewContainer {
        ClearResultButton(
            onClick = {},
        )
    }
}
