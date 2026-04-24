package com.example.android.playground.roomdatabase.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
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
import com.example.android.playground.roomdatabase.presentation.model.AuthorWithBooksUiModel

/**
 * Displays an author's name, contact info (@Embedded in the DB), and their list of books.
 * Demonstrates: @Embedded (contact fields stored in the same "authors" table row)
 *               + @Relation one-to-many (author → books).
 */
@Composable
internal fun AuthorCard(
    author: AuthorWithBooksUiModel,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Author name
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = author.authorName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // @Embedded contact info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 4.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = author.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 4.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = author.website,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            if (author.books.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Books (${author.books.size})",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    author.books.forEach { title ->
                        Text(
                            text = "• $title",
                            style = MaterialTheme.typography.bodyMedium,
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
private fun AuthorCardPreview() {
    PreviewContainer {
        AuthorCard(
            author = AuthorWithBooksUiModel(
                authorName = "Robert C. Martin",
                email = "uncle.bob@cleancode.com",
                website = "cleancoder.com",
                books = listOf("Clean Code", "Clean Architecture"),
            ),
        )
    }
}

@DualThemePreview
@Composable
private fun AuthorCardNoBooksPreview() {
    PreviewContainer {
        AuthorCard(
            author = AuthorWithBooksUiModel(
                authorName = "New Author",
                email = "new@author.com",
                website = "newauthor.com",
                books = emptyList(),
            ),
        )
    }
}
