package com.example.android.systemdesign.note.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.android.systemdesign.note.presentation.component.NoteMetadataCard
import com.example.android.systemdesign.note.presentation.component.NoteTextField
import com.example.android.systemdesign.note.presentation.intent.NoteDetailIntent
import com.example.android.systemdesign.note.presentation.sideeffect.NoteDetailSideEffect
import com.example.android.systemdesign.note.presentation.state.NoteDetailState
import com.example.android.systemdesign.note.presentation.viewmodel.NoteDetailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: NoteDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is NoteDetailSideEffect.NavigateBack -> onNavigateBack()
                is NoteDetailSideEffect.ShowSuccessMessage ->  {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(sideEffect.message)
                    }
                }
                is NoteDetailSideEffect.ShowErrorMessage ->  {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(sideEffect.message)
                    }
                }
            }
        }
    }

    NoteDetailScreenContent(
        modifier = modifier,
        state = state,
        snackbarHostState = snackbarHostState,
        onNavigateBack = { viewModel.handleIntent(NoteDetailIntent.NavigateBack) },
        onEditSave = {
            if (state.isEditing) {
                viewModel.handleIntent(NoteDetailIntent.SaveNote)
            } else {
                viewModel.handleIntent(NoteDetailIntent.EditNote)
            }
        },
        onTitleChange = { viewModel.handleIntent(NoteDetailIntent.UpdateTitle(it)) },
        onContentChange = { viewModel.handleIntent(NoteDetailIntent.UpdateContent(it)) },
        onCancel = { viewModel.handleIntent(NoteDetailIntent.CancelEditing) },
        onErrorDismiss = { viewModel.handleIntent(NoteDetailIntent.ClearError) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreenContent(
    modifier: Modifier = Modifier,
    state: NoteDetailState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onNavigateBack: () -> Unit = {},
    onEditSave: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onContentChange: (String) -> Unit = {},
    onCancel: () -> Unit = {},
    onErrorDismiss: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = if (state.note != null) "Note Details" else "New Note",
                onNavigationClick = onNavigateBack,
                actions = {
                    IconButton(onClick = onEditSave) {
                        Icon(
                            imageVector = if (state.isEditing) Icons.Default.Save else Icons.Default.Edit,
                            contentDescription = if (state.isEditing) "Save" else "Edit"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Error handling using extracted component
                state.error?.let { errorMessage ->
                    NoteErrorCard(
                        errorMessage = errorMessage,
                        onDismiss = onErrorDismiss
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
                } else {
                    // Title
                    if (state.isEditing) {
                        NoteTextField(
                            value = state.title,
                            onValueChange = onTitleChange,
                            label = "Title"
                        )
                    } else {
                        Text(
                            text = state.title,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 16.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Content
                    if (state.isEditing) {
                        NoteTextField(
                            value = state.content,
                            onValueChange = onContentChange,
                            label = "Content",
                            isMultiline = true,
                            maxLines = 10
                        )
                    } else {
                        if (state.content.isNotBlank()) {
                            Text(
                                text = state.content,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 16.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Metadata using extracted component
                    state.note?.let { note ->
                        if (!state.isEditing) {
                            NoteMetadataCard(
                                createdAt = note.createdAt,
                                updatedAt = note.updatedAt
                            )
                        }
                    }

                    // Cancel button when editing
                    if (state.isEditing && state.note != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(
                            onClick = onCancel,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }
}


// Preview functions using the new preview system
@DualThemePreview
@Composable
private fun NoteDetailScreenPreview() {
    PreviewContainer {
        NoteDetailScreenContent(
            state = NoteDetailState(
                note = Note(
                    id = 1,
                    title = "Sample Note Title",
                    content = "This is sample content for the note item to demonstrate how it looks in the theme.",
                    createdAt = System.currentTimeMillis() - 86400000, // 1 day ago
                    updatedAt = System.currentTimeMillis() - 3600000   // 1 hour ago
                ),
                title = "Sample Note Title",
                content = "This is sample content for the note item to demonstrate how it looks in the theme."
            )
        )
    }
}

@DualThemePreview
@Composable
private fun NoteDetailScreenDarkPreview() {
    PreviewContainer(darkTheme = true) {
        NoteDetailScreenContent(
            state = NoteDetailState(
                note = Note(
                    id = 1,
                    title = "Sample Note Title",
                    content = "This is sample content for the note item to demonstrate how it looks in the dark theme.",
                    createdAt = System.currentTimeMillis() - 86400000, // 1 day ago
                    updatedAt = System.currentTimeMillis() - 3600000   // 1 hour ago
                ),
                title = "Sample Note Title",
                content = "This is sample content for the note item to demonstrate how it looks in the dark theme."
            )
        )
    }
}

@DualThemePreview
@Composable
private fun NoteDetailEditModePreview() {
    PreviewContainer {
        NoteDetailScreenContent(
            state = NoteDetailState(
                note = Note(
                    id = 1,
                    title = "Sample Note Title",
                    content = "This is sample content for editing mode",
                    createdAt = System.currentTimeMillis() - 86400000,
                    updatedAt = System.currentTimeMillis() - 3600000
                ),
                title = "Sample Note Title",
                content = "This is sample content for editing mode",
                isEditing = true
            )
        )
    }
}

@DualThemePreview
@Composable
private fun NoteDetailNewNotePreview() {
    PreviewContainer {
        NoteDetailScreenContent(
            state = NoteDetailState(
                note = null,
                title = "",
                content = "",
                isEditing = true
            )
        )
    }
}

@DualThemePreview
@Composable
private fun NoteDetailLoadingPreview() {
    PreviewContainer {
        NoteDetailScreenContent(
            state = NoteDetailState(
                isLoading = true
            )
        )
    }
}
