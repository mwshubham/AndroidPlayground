package com.example.android.playground.sse.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.sse.presentation.intent.SseIntent
import com.example.android.playground.sse.presentation.state.SseConnectionStatus
import com.example.android.playground.sse.presentation.state.SseState
import com.example.android.playground.sse.presentation.state.WikipediaChangeUiModel

private object StatusColors {
    val Positive = Color(color = 0xFF2E7D32)
    val Warning = Color(color = 0xFFF57C00)
    val Neutral = Color(color = 0xFF757575)
    val Negative = Color(color = 0xFFC62828)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SseContent(
    state: SseState,
    onIntent: (SseIntent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SSE — Wikipedia Changes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (state.changes.isNotEmpty()) {
                        TextButton(onClick = { onIntent(SseIntent.Clear) }) {
                            Text("Clear")
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            SseStatusChip(status = state.connectionStatus)
            Spacer(modifier = Modifier.height(12.dp))
            SubscribeButton(
                status = state.connectionStatus,
                onSubscribe = { onIntent(SseIntent.Subscribe) },
                onUnsubscribe = { onIntent(SseIntent.Unsubscribe) },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Streaming live edits from stream.wikimedia.org",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (state.changes.isEmpty()) {
                Text(
                    text = "Subscribe to see real-time Wikipedia changes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.changes) { change ->
                        WikipediaChangeItem(change = change)
                    }
                }
            }
        }
    }
}

@Composable
private fun SseStatusChip(status: SseConnectionStatus) {
    val (label, color) =
        when (status) {
            SseConnectionStatus.Connected -> "Streaming" to StatusColors.Positive
            SseConnectionStatus.Connecting -> "Connecting\u2026" to StatusColors.Warning
            SseConnectionStatus.Disconnected -> "Disconnected" to StatusColors.Neutral
            SseConnectionStatus.Error -> "Error" to StatusColors.Negative
        }
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.15f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .size(8.dp)
                        .background(color, RoundedCornerShape(4.dp)),
            )
            Text(text = label, color = color, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun SubscribeButton(
    status: SseConnectionStatus,
    onSubscribe: () -> Unit,
    onUnsubscribe: () -> Unit,
) {
    val isSubscribed =
        status == SseConnectionStatus.Connected || status == SseConnectionStatus.Connecting
    Button(
        onClick = if (isSubscribed) onUnsubscribe else onSubscribe,
        modifier = Modifier.fillMaxWidth(),
        colors =
            ButtonDefaults.buttonColors(
                containerColor =
                    if (isSubscribed) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
            ),
    ) {
        Text(if (isSubscribed) "Unsubscribe" else "Subscribe to Wikipedia SSE")
    }
}

@ComponentPreview
@Composable
private fun SseContentPreview() {
    PreviewContainer {
        SseContent(
            state =
                SseState(
                    connectionStatus = SseConnectionStatus.Connected,
                    changes =
                        listOf(
                            WikipediaChangeUiModel(
                                title = "Albert Einstein",
                                user = "WikiEditor42",
                                wiki = "enwiki",
                                type = "edit",
                                comment = "Fixed citation format",
                                formattedTime = "14:32:01",
                            ),
                            WikipediaChangeUiModel(
                                title = "Paris",
                                user = "AnonymousUser",
                                wiki = "frwiki",
                                type = "new",
                                comment = "Added population data for 2024",
                                formattedTime = "14:31:58",
                            ),
                        ),
                ),
            onIntent = {},
            onNavigateBack = {},
        )
    }
}
