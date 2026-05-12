package com.example.android.playground.websocket.presentation.component

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.websocket.presentation.intent.WebSocketIntent
import com.example.android.playground.websocket.presentation.state.BtcTickerUiModel
import com.example.android.playground.websocket.presentation.state.WebSocketConnectionStatus
import com.example.android.playground.websocket.presentation.state.WebSocketState

private object StatusColors {
    val Positive = Color(color = 0xFF2E7D32)
    val Warning = Color(color = 0xFFF57C00)
    val Neutral = Color(color = 0xFF757575)
    val Negative = Color(color = 0xFFC62828)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WebSocketContent(
    state: WebSocketState,
    onIntent: (WebSocketIntent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WebSocket — BTC/USDT") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            ConnectionStatusChip(status = state.connectionStatus)
            Spacer(modifier = Modifier.height(16.dp))
            PriceCard(state = state)
            Spacer(modifier = Modifier.height(16.dp))
            ConnectButton(
                status = state.connectionStatus,
                onConnect = { onIntent(WebSocketIntent.Connect) },
                onDisconnect = { onIntent(WebSocketIntent.Disconnect) },
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (state.recentTicks.isNotEmpty()) {
                Text(
                    text = "Recent Updates",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(state.recentTicks) { tick ->
                        TickerHistoryItem(tick = tick)
                    }
                }
            }
        }
    }
}

@Composable
private fun ConnectionStatusChip(status: WebSocketConnectionStatus) {
    val (label, color) =
        when (status) {
            WebSocketConnectionStatus.Connected -> "Connected" to StatusColors.Positive
            WebSocketConnectionStatus.Connecting -> "Connecting\u2026" to StatusColors.Warning
            WebSocketConnectionStatus.Disconnected -> "Disconnected" to StatusColors.Neutral
            WebSocketConnectionStatus.Error -> "Error" to StatusColors.Negative
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
private fun PriceCard(state: WebSocketState) {
    val priceColor = if (state.isPositive) StatusColors.Positive else StatusColors.Negative
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "BTC / USDT",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.price,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = priceColor,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${state.priceChange}  (${state.priceChangePercent})",
                style = MaterialTheme.typography.bodyMedium,
                color = priceColor,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                PriceStat(label = "24h High", value = state.highPrice)
                PriceStat(label = "24h Low", value = state.lowPrice)
            }
        }
    }
}

@Composable
private fun PriceStat(
    label: String,
    value: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Monospace,
        )
    }
}

@Composable
private fun ConnectButton(
    status: WebSocketConnectionStatus,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
) {
    val isConnected =
        status == WebSocketConnectionStatus.Connected ||
            status == WebSocketConnectionStatus.Connecting
    Button(
        onClick = if (isConnected) onDisconnect else onConnect,
        modifier = Modifier.fillMaxWidth(),
        colors =
            ButtonDefaults.buttonColors(
                containerColor =
                    if (isConnected) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
            ),
    ) {
        Text(if (isConnected) "Disconnect" else "Connect to Binance")
    }
}

@ComponentPreview
@Composable
private fun WebSocketContentPreview() {
    PreviewContainer {
        WebSocketContent(
            state =
                WebSocketState(
                    connectionStatus = WebSocketConnectionStatus.Connected,
                    price = "$67,432.10",
                    priceChange = "+1234.50",
                    priceChangePercent = "+1.85%",
                    highPrice = "$68,000.00",
                    lowPrice = "$65,800.00",
                    isPositive = true,
                    recentTicks =
                        listOf(
                            BtcTickerUiModel("$67,432.10", "+1234.50", "+1.85%", true, "14:32:01"),
                            BtcTickerUiModel("$67,400.00", "+1202.40", "+1.82%", true, "14:32:00"),
                        ),
                ),
            onIntent = {},
            onNavigateBack = {},
        )
    }
}
