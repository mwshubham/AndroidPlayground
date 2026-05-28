package com.example.android.playground.roomdatabase.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.roomdatabase.presentation.model.BookWithTagsUiModel

/**
 * Displays a book's details and its associated tags.
 * Demonstrates: @TypeConverter (genres stored as List<String> in DB)
 *               + @Relation many-to-many via Junction/BookTagCrossRef.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun BookWithTagsCard(
    book: BookWithTagsUiModel,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${book.publishYear}  ·  ${book.genres}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (book.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tags",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    book.tags.forEach { tag ->
                        AssistChip(
                            onClick = {},
                            label = { Text(text = tag, style = MaterialTheme.typography.labelSmall) },
                        )
                    }
                }
            }
        }
    }
}

// ---- Previews ----

@DualThemePreview
@Composable
private fun BookWithTagsCardPreview() {
    PreviewContainer {
        BookWithTagsCard(
            book =
                BookWithTagsUiModel(
                    title = "Clean Architecture",
                    publishYear = 2017,
                    genres = "Software Engineering · Architecture",
                    tags = listOf("Must Read", "Advanced", "Patterns"),
                ),
        )
    }
}

@DualThemePreview
@Composable
private fun BookWithTagsCardNoTagsPreview() {
    PreviewContainer {
        BookWithTagsCard(
            book =
                BookWithTagsUiModel(
                    title = "A Book With No Tags",
                    publishYear = 2020,
                    genres = "Fiction",
                    tags = emptyList(),
                ),
        )
    }
}
