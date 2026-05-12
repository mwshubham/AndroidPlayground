package com.example.android.playground.websocket.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.websocket.presentation.state.BtcTickerUiModel

private object TickerColors {
    val Positive = Color(color = 0xFF2E7D32)
    val Negative = Color(color = 0xFFC62828)
}

@Composable
internal fun TickerHistoryItem(
    tick: BtcTickerUiModel,
    modifier: Modifier = Modifier,
) {
    val priceColor = if (tick.isPositive) TickerColors.Positive else TickerColors.Negative
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = tick.price,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                color = priceColor,
            )
            Text(
                text = tick.priceChangePercent,
                style = MaterialTheme.typography.bodySmall,
                color = priceColor,
            )
            Text(
                text = tick.formattedTime,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = FontFamily.Monospace,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun TickerHistoryItemPreview() {
    PreviewContainer {
        TickerHistoryItem(
            tick =
                BtcTickerUiModel(
                    price = "$67,432.10",
                    priceChange = "+1234.50",
                    priceChangePercent = "+1.85%",
                    isPositive = true,
                    formattedTime = "14:32:01",
                ),
        )
    }
}
