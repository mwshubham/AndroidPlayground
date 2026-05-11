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
import com.example.android.playground.note.presentation.model.NoteListItemUiModel
import com.example.android.playground.note.presentation.state.NoteListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoteListContent(
    modifier: Modifier = Modifier,
    state: NoteListState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onNavigateBack: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onNoteClick: (Long) -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onDeleteNote: (Long) -> Unit = {},
    onErrorDismiss: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = "Notes",
                onNavigationClick = onNavigateBack,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
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
                onSearchQueryChange = onSearchQueryChange,
            )

            state.error?.let { error ->
                NoteErrorCard(
                    errorMessage = error,
                    onDismiss = onErrorDismiss,
                )
            }

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
                        onNoteClick = { onNoteClick(note.id) },
                        onDeleteClick = { onDeleteNote(note.id) },
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
                    error = null,
                ),
        )
    }
}

@DualThemePreview
@Composable
private fun NoteListContentLoadingPreview() {
    PreviewContainer {
        NoteListContent(
            state =
                NoteListState(
                    searchQuery = "",
                    isLoading = true,
                    error = null,
                ),
        )
    }
}

@DualThemePreview
@Composable
private fun NoteListContentEmptyPreview() {
    PreviewContainer {
        NoteListContent(
            state =
                NoteListState(
                    searchQuery = "",
                    isLoading = false,
                    error = null,
                ),
        )
    }
}
