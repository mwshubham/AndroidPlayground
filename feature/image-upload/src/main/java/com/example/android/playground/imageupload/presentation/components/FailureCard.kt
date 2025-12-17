package com.example.android.playground.imageupload.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.imageupload.presentation.ImageUploadState

@Composable
fun RowScope.FailureCard(
    modifier: Modifier = Modifier,
    state: ImageUploadState,
) {
    Card(
        modifier = modifier.weight(1f),
        colors =
            CardDefaults.cardColors(
                containerColor = Color(0xFFf44336).copy(alpha = 0.1f),
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFFf44336)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Failure",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFc62828),
            )

            Text(
                text = "${state.failureCount}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFc62828),
            )
        }
    }
}

@ComponentPreview
@Composable
fun FailureCardPreview() {
    PreviewContainer {
        Row {
            FailureCard(
                state =
                    ImageUploadState(
                        failureCount = 30,
                    ),
            )
        }
    }
}
