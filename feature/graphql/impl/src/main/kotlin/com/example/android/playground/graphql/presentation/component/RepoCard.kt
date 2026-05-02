package com.example.android.playground.graphql.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.graphql.presentation.model.RepoUiModel

@Composable
fun RepoCard(
    repo: RepoUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = repo.nameWithOwner,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = repo.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Stars",
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                    Text(
                        text = repo.starsDisplay,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
                repo.language?.let { lang ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        val langColor =
                            repo.languageColor?.parseHexColor()
                                ?: MaterialTheme.colorScheme.primary
                        Icon(
                            imageVector = Icons.Default.Circle,
                            contentDescription = null,
                            modifier = Modifier.size(10.dp),
                            tint = langColor,
                        )
                        Spacer(Modifier.width(0.dp))
                        Text(
                            text = lang,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
        }
    }
}

private fun String.parseHexColor(): Color? =
    runCatching {
        Color(this.toColorInt())
    }.getOrNull()

// ── Previews ────────────────────────────────────────────────────────────────

@ComponentPreview
@Composable
private fun RepoCardPreview() {
    PreviewContainer {
        RepoCard(
            repo =
                RepoUiModel(
                    name = "architecture-samples",
                    nameWithOwner = "android/architecture-samples",
                    description = "A collection of samples to discuss and showcase different architectural tools and approaches for Android apps.",
                    starsDisplay = "44.2k",
                    language = "Kotlin",
                    languageColor = "#A97BFF",
                    url = "https://github.com/android/architecture-samples",
                    ownerLogin = "android",
                ),
            onClick = {},
        )
    }
}

@ComponentPreview
@Composable
private fun RepoCardNoLanguagePreview() {
    PreviewContainer {
        RepoCard(
            repo =
                RepoUiModel(
                    name = "awesome-android",
                    nameWithOwner = "JStumpp/awesome-android",
                    description = "A curated list of awesome Android packages and resources.",
                    starsDisplay = "11.5k",
                    language = null,
                    languageColor = null,
                    url = "https://github.com/JStumpp/awesome-android",
                    ownerLogin = "JStumpp",
                ),
            onClick = {},
        )
    }
}
