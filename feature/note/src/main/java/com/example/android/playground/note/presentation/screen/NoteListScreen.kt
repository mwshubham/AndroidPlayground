package com.example.android.playground.note.presentation.screen

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.note.domain.model.Note
import com.example.android.playground.note.presentation.component.NoteErrorCard
import com.example.android.playground.note.presentation.component.NoteItem
import com.example.android.playground.note.presentation.component.NoteSearchBar
import com.example.android.playground.note.presentation.intent.NoteListIntent
import com.example.android.playground.note.presentation.sideeffect.NoteListSideEffect
import com.example.android.playground.note.presentation.state.NoteListState
import com.example.android.playground.note.presentation.viewmodel.NoteListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
    viewModel: NoteListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val filteredNotes by remember(state.searchQuery, state.notes) {
        derivedStateOf {
            if (state.searchQuery.isBlank()) {
                state.notes
            } else {
                state.notes.filter { note ->
                    note.title.contains(state.searchQuery, ignoreCase = true) ||
                        note.content.contains(state.searchQuery, ignoreCase = true)
                }
            }
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is NoteListSideEffect.NavigateToNoteDetail -> onNavigateToDetail(sideEffect.noteId)
                is NoteListSideEffect.NavigateToAddNote -> onNavigateToAdd()
                is NoteListSideEffect.NavigateBack -> onNavigateBack()

                is NoteListSideEffect.ShowSuccessMessage -> snackbarHostState.showSnackbar(sideEffect.message)
                is NoteListSideEffect.ShowErrorMessage -> snackbarHostState.showSnackbar(sideEffect.message)
            }
        }
    }

    NoteListScreenContent(
        modifier = modifier,
        state = state,
        filteredNotes = filteredNotes,
        snackbarHostState = snackbarHostState,
        onNavigateBack = onNavigateBack,
        onAddClick = { viewModel.handleIntent(NoteListIntent.NavigateToAdd) },
        onNoteClick = { noteId -> viewModel.handleIntent(NoteListIntent.NavigateToDetail(noteId)) },
        onSearchQueryChange = { query -> viewModel.handleIntent(NoteListIntent.SearchNotes(query)) },
        onDeleteNote = { noteId -> viewModel.handleIntent(NoteListIntent.DeleteNote(noteId)) },
        onErrorDismiss = { viewModel.handleIntent(NoteListIntent.ClearError) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreenContent(
    modifier: Modifier = Modifier,
    state: NoteListState,
    filteredNotes: List<Note>,
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
            // Search Bar using extracted component
            NoteSearchBar(
                searchQuery = state.searchQuery,
                onSearchQueryChange = onSearchQueryChange,
            )

            // Error handling using extracted component
            state.error?.let { error ->
                NoteErrorCard(
                    errorMessage = error,
                    onDismiss = onErrorDismiss,
                )
            }

            // Loading indicator
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
                items(filteredNotes) { note ->
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

// Preview functions using the new preview system
@DualThemePreview
@Composable
private fun NoteListScreenPreview() {
    PreviewContainer {
        NoteListScreenContent(
            state =
                NoteListState(
                    searchQuery = "",
                    isLoading = false,
                    error = null,
                ),
            filteredNotes =
                listOf(
                    Note(
                        id = 1L,
                        title = "Complete project proposal",
                        content = "Finish writing the project proposal for the new mobile app",
                        createdAt = System.currentTimeMillis() - 86400000, // 1 day ago
                        updatedAt = System.currentTimeMillis() - 3600000, // 1 hour ago
                    ),
                    Note(
                        id = 2L,
                        title = "Buy groceries",
                        content = "Milk, bread, eggs, vegetables, and fruits for the week",
                        createdAt = System.currentTimeMillis() - 172800000, // 2 days ago
                        updatedAt = System.currentTimeMillis() - 7200000, // 2 hours ago
                    ),
                    Note(
                        id = 3L,
                        title = "Call dentist",
                        content = "",
                        createdAt = System.currentTimeMillis() - 259200000, // 3 days ago
                        updatedAt = System.currentTimeMillis() - 10800000, // 3 hours ago
                    ),
                ),
        )
    }
}

@DualThemePreview
@Composable
private fun NoteListScreenDarkPreview() {
    PreviewContainer(darkTheme = true) {
        NoteListScreenContent(
            state =
                NoteListState(
                    searchQuery = "",
                    isLoading = false,
                    error = null,
                ),
            filteredNotes =
                listOf(
                    Note(
                        id = 1L,
                        title = "Complete project proposal",
                        content = "Finish writing the project proposal for the new mobile app",
                        createdAt = System.currentTimeMillis() - 86400000,
                        updatedAt = System.currentTimeMillis() - 3600000,
                    ),
                ),
        )
    }
}

@DualThemePreview
@Composable
private fun NoteListScreenLoadingPreview() {
    PreviewContainer {
        NoteListScreenContent(
            state =
                NoteListState(
                    searchQuery = "",
                    isLoading = true,
                    error = null,
                ),
            filteredNotes = emptyList(),
        )
    }
}

@DualThemePreview
@Composable
private fun NoteListScreenEmptyPreview() {
    PreviewContainer {
        NoteListScreenContent(
            state =
                NoteListState(
                    searchQuery = "",
                    isLoading = false,
                    error = null,
                ),
            filteredNotes = emptyList(),
        )
    }
}
