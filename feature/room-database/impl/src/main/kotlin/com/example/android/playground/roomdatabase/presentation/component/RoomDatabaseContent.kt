package com.example.android.playground.roomdatabase.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.roomdatabase.presentation.intent.RoomDatabaseIntent
import com.example.android.playground.roomdatabase.presentation.model.AuthorWithBooksUiModel
import com.example.android.playground.roomdatabase.presentation.model.BookWithTagsUiModel
import com.example.android.playground.roomdatabase.presentation.model.RoomDatabaseTab
import com.example.android.playground.roomdatabase.presentation.state.RoomDatabaseState

@Composable
internal fun RoomDatabaseContent(
    state: RoomDatabaseState,
    onIntent: (RoomDatabaseIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Room Database",
                onNavigationClick = { onIntent(RoomDatabaseIntent.NavigateBack) },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = state.error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            else -> {
                RoomDatabaseTabbedContent(
                    state = state,
                    onIntent = onIntent,
                    paddingValues = paddingValues,
                )
            }
        }
    }
}

@Composable
private fun RoomDatabaseTabbedContent(
    state: RoomDatabaseState,
    onIntent: (RoomDatabaseIntent) -> Unit,
    paddingValues: PaddingValues,
) {
    val tabs = RoomDatabaseTab.entries
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            TabRow(selectedTabIndex = tabs.indexOf(state.selectedTab)) {
                tabs.forEach { tab ->
                    Tab(
                        selected = state.selectedTab == tab,
                        onClick = { onIntent(RoomDatabaseIntent.OnTabSelected(tab)) },
                        text = {
                            Text(
                                text = when (tab) {
                                    RoomDatabaseTab.AUTHORS -> "Authors"
                                    RoomDatabaseTab.BOOKS_WITH_TAGS -> "Books & Tags"
                                },
                            )
                        },
                    )
                }
            }
        }

        when (state.selectedTab) {
            RoomDatabaseTab.AUTHORS -> {
                item {
                    ConceptInfoBanner(
                        title = "@Embedded + @Relation (One-to-Many)",
                        description = "ContactInfo fields (email, website) are @Embedded — stored flat in the \"authors\" table. Each author is loaded with their books via a @Transaction @Relation query, avoiding N+1 fetches.",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                items(state.authorsWithBooks, key = { it.authorName }) { author ->
                    AuthorCard(author = author)
                }
            }

            RoomDatabaseTab.BOOKS_WITH_TAGS -> {
                item {
                    ConceptInfoBanner(
                        title = "@TypeConverter + @Relation (Many-to-Many via Junction)",
                        description = "Genres are stored as a comma-separated String via @TypeConverter (List<String> ↔ String). Tags use a BookTagCrossRef join table; Room resolves them via associateBy = Junction(BookTagCrossRef::class).",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                items(state.booksWithTags, key = { it.title }) { book ->
                    BookWithTagsCard(book = book)
                }
            }
        }
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun RoomDatabaseContentAuthorsPreview() {
    PreviewContainer {
        RoomDatabaseContent(
            state = RoomDatabaseState(
                selectedTab = RoomDatabaseTab.AUTHORS,
                authorsWithBooks = listOf(
                    AuthorWithBooksUiModel(
                        authorName = "Robert C. Martin",
                        email = "uncle.bob@cleancode.com",
                        website = "cleancoder.com",
                        books = listOf("Clean Code", "Clean Architecture"),
                    ),
                    AuthorWithBooksUiModel(
                        authorName = "Martin Fowler",
                        email = "fowler@martinfowler.com",
                        website = "martinfowler.com",
                        books = listOf("Refactoring", "Patterns of Enterprise Application Architecture"),
                    ),
                ),
            ),
            onIntent = {},
        )
    }
}

@ComponentPreview
@Composable
private fun RoomDatabaseContentBooksPreview() {
    PreviewContainer {
        RoomDatabaseContent(
            state = RoomDatabaseState(
                selectedTab = RoomDatabaseTab.BOOKS_WITH_TAGS,
                booksWithTags = listOf(
                    BookWithTagsUiModel(
                        title = "Clean Architecture",
                        publishYear = 2017,
                        genres = "Software Engineering · Architecture",
                        tags = listOf("Must Read", "Advanced", "Patterns"),
                    ),
                    BookWithTagsUiModel(
                        title = "The C Programming Language",
                        publishYear = 1978,
                        genres = "Programming Languages · Systems",
                        tags = listOf("Classic", "Must Read"),
                    ),
                ),
            ),
            onIntent = {},
        )
    }
}

@ComponentPreview
@Composable
private fun RoomDatabaseContentLoadingPreview() {
    PreviewContainer {
        RoomDatabaseContent(
            state = RoomDatabaseState(isLoading = true),
            onIntent = {},
        )
    }
}
