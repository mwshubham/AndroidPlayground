package com.example.android.playground.flowlivedata.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer

@Composable
internal fun LateSubscriberCard(
    result: String,
    onSimulate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Late Subscriber Demo",
                style = MaterialTheme.typography.titleSmall,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "StateFlow always holds its latest value. A brand-new collector receives it instantly — even if it subscribes after many emissions have already occurred.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = result,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onSimulate,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Simulate Late Subscribe")
            }
        }
    }
}

@DualThemePreview
@Composable
private fun LateSubscriberCardPreview() {
    PreviewContainer {
        LateSubscriberCard(
            result = "Late subscriber got: 7 at 10:00:05",
            onSimulate = {},
        )
    }
}
