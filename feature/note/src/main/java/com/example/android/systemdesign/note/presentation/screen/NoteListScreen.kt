package com.example.android.systemdesign.note.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.systemdesign.core.ui.components.AppTopAppBar
import com.example.android.systemdesign.core.ui.preview.DualThemePreview
import com.example.android.systemdesign.core.ui.preview.PreviewContainer
import com.example.android.systemdesign.note.domain.model.Note
import com.example.android.systemdesign.note.presentation.component.NoteErrorCard
import com.example.android.systemdesign.note.presentation.component.NoteItem
import com.example.android.systemdesign.note.presentation.component.NoteSearchBar
import com.example.android.systemdesign.note.presentation.state.NoteListState
import com.example.android.systemdesign.note.presentation.viewmodel.NoteListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
    viewModel: NoteListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val filteredNotes by viewModel.filteredNotes.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Notes",
                onNavigationClick = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Search Bar using extracted component
            NoteSearchBar(
                searchQuery = state.searchQuery,
                onSearchQueryChange = viewModel::updateSearchQuery
            )

            // Error handling using extracted component
            state.error?.let { error ->
                NoteErrorCard(
                    errorMessage = error,
                    onDismiss = viewModel::clearError
                )
            }

            // Loading indicator
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredNotes) { note ->
                    NoteItem(
                        note = note,
                        onNoteClick = { onNavigateToDetail(note.id) },
                        onDeleteClick = { viewModel.deleteNote(note) }
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
            state = NoteListState(
                searchQuery = "",
                isLoading = false,
                error = null
            ),
            filteredNotes = listOf(
                Note(
                    id = 1L,
                    title = "Complete project proposal",
                    content = "Finish writing the project proposal for the new mobile app",
                    createdAt = System.currentTimeMillis() - 86400000, // 1 day ago
                    updatedAt = System.currentTimeMillis() - 3600000    // 1 hour ago
                ),
                Note(
                    id = 2L,
                    title = "Buy groceries",
                    content = "Milk, bread, eggs, vegetables, and fruits for the week",
                    createdAt = System.currentTimeMillis() - 172800000, // 2 days ago
                    updatedAt = System.currentTimeMillis() - 7200000     // 2 hours ago
                ),
                Note(
                    id = 3L,
                    title = "Call dentist",
                    content = "",
                    createdAt = System.currentTimeMillis() - 259200000, // 3 days ago
                    updatedAt = System.currentTimeMillis() - 10800000    // 3 hours ago
                )
            ),
            onNavigateToDetail = {},
            onNavigateToAdd = {},
            onSearchQueryChange = {},
            onDeleteNote = {},
            onClearError = {}
        )
    }
}

@DualThemePreview
@Composable
private fun NoteListScreenDarkPreview() {
    PreviewContainer(darkTheme = true) {
        NoteListScreenContent(
            state = NoteListState(
                searchQuery = "",
                isLoading = false,
                error = null
            ),
            filteredNotes = listOf(
                Note(
                    id = 1L,
                    title = "Complete project proposal",
                    content = "Finish writing the project proposal for the new mobile app",
                    createdAt = System.currentTimeMillis() - 86400000,
                    updatedAt = System.currentTimeMillis() - 3600000
                )
            ),
            onNavigateToDetail = {},
            onNavigateToAdd = {},
            onSearchQueryChange = {},
            onDeleteNote = {},
            onClearError = {}
        )
    }
}

@Composable
private fun NoteListScreenContent(
    state: NoteListState,
    filteredNotes: List<Note>,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onDeleteNote: (Note) -> Unit,
    onClearError: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            AppTopAppBar(
                title = "Notes",
                onNavigationClick = {}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Search Bar using extracted component
            NoteSearchBar(
                searchQuery = state.searchQuery,
                onSearchQueryChange = onSearchQueryChange
            )

            // Error handling using extracted component
            state.error?.let { error ->
                NoteErrorCard(
                    errorMessage = error,
                    onDismiss = onClearError
                )
            }

            // Loading indicator
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredNotes) { note ->
                    NoteItem(
                        note = note,
                        onNoteClick = { onNavigateToDetail(note.id) },
                        onDeleteClick = { onDeleteNote(note) }
                    )
                }
            }
        }
    }
}
