package com.example.android.playground.note.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.note.presentation.intent.NoteListIntent
import com.example.android.playground.note.presentation.model.NoteListItemUiModel
import com.example.android.playground.note.presentation.state.NoteListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoteListContent(
    state: NoteListState,
    onIntent: (NoteListIntent) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = "Notes",
                onNavigationClick = { onIntent(NoteListIntent.NavigateBack) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onIntent(NoteListIntent.NavigateToAdd) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
        ) {
            NoteSearchBar(
                searchQuery = state.searchQuery,
                onSearchQueryChange = { onIntent(NoteListIntent.SearchNotes(it)) },
            )

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.notes) { note ->
                    NoteItem(
                        note = note,
                        onNoteClick = { onIntent(NoteListIntent.NavigateToDetail(note.id)) },
                        onDeleteClick = { onIntent(NoteListIntent.DeleteNote(note.id)) },
                    )
                }
            }
        }
    }
}

// ---- Previews ----

@DualThemePreview
@Composable
private fun NoteListContentPreview() {
    PreviewContainer {
        NoteListContent(
            state =
                NoteListState(
                    notes =
                        listOf(
                            NoteListItemUiModel(
                                id = 1L,
                                title = "Complete project proposal",
                                content = "Finish writing the project proposal for the new mobile app",
                                updatedAtFormatted = "1 hour ago",
                            ),
                            NoteListItemUiModel(
                                id = 2L,
                                title = "Buy groceries",
                                content = "Milk, bread, eggs, vegetables, and fruits for the week",
                                updatedAtFormatted = "2 hours ago",
                            ),
                            NoteListItemUiModel(
                                id = 3L,
                                title = "Call dentist",
                                content = "",
                                updatedAtFormatted = "3 hours ago",
                            ),
                        ),
                    searchQuery = "",
                    isLoading = false,
                ),
            onIntent = {},
        )
    }
}

@DualThemePreview
@Composable
private fun NoteListContentLoadingPreview() {
    PreviewContainer {
        NoteListContent(
            state = NoteListState(searchQuery = "", isLoading = true),
            onIntent = {},
        )
    }
}

@DualThemePreview
@Composable
private fun NoteListContentEmptyPreview() {
    PreviewContainer {
        NoteListContent(
            state = NoteListState(searchQuery = "", isLoading = false),
            onIntent = {},
        )
    }
}
