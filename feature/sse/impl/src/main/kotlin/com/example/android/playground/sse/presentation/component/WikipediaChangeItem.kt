package com.example.android.playground.sse.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.sse.presentation.state.WikipediaChangeUiModel

@Composable
internal fun WikipediaChangeItem(
    change: WikipediaChangeUiModel,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = change.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = change.formattedTime,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement =
                    androidx.compose.foundation.layout.Arrangement
                        .spacedBy(8.dp),
            ) {
                TypeBadge(type = change.type)
                Text(
                    text = change.wiki,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "by ${change.user}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (change.comment.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = change.comment,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun TypeBadge(type: String) {
    val color =
        when (type) {
            "edit" -> MaterialTheme.colorScheme.primary
            "new" -> MaterialTheme.colorScheme.tertiary
            else -> MaterialTheme.colorScheme.secondary
        }
    Text(
        text = type.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = color,
        fontWeight = FontWeight.Bold,
    )
}

@ComponentPreview
@Composable
private fun WikipediaChangeItemPreview() {
    PreviewContainer {
        WikipediaChangeItem(
            change =
                WikipediaChangeUiModel(
                    title = "Albert Einstein",
                    user = "WikiEditor42",
                    wiki = "enwiki",
                    type = "edit",
                    comment = "Fixed citation format in references section",
                    formattedTime = "14:32:01",
                ),
        )
    }
}
