package com.example.android.playground.note.presentation.component

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.note.presentation.intent.NoteDetailIntent
import com.example.android.playground.note.presentation.model.NoteDetailUiModel
import com.example.android.playground.note.presentation.state.NoteDetailState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoteDetailContent(
    state: NoteDetailState,
    onIntent: (NoteDetailIntent) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = if (state.note != null) "Note Details" else "New Note",
                onNavigationClick = { onIntent(NoteDetailIntent.NavigateBack) },
                actions = {
                    IconButton(
                        onClick = {
                            onIntent(
                                if (state.isEditing) NoteDetailIntent.SaveNote else NoteDetailIntent.EditNote,
                            )
                        },
                    ) {
                        Icon(
                            imageVector = if (state.isEditing) Icons.Default.Save else Icons.Default.Edit,
                            contentDescription = if (state.isEditing) "Save" else "Edit",
                        )
                    }
                },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
            ) {
                state.error?.let { errorMessage ->
                    NoteErrorCard(
                        errorMessage = errorMessage,
                        onDismiss = { onIntent(NoteDetailIntent.ClearError) },
                    )
                }

                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    if (state.isEditing) {
                        NoteTextField(
                            value = state.title,
                            onValueChange = { onIntent(NoteDetailIntent.UpdateTitle(it)) },
                            label = "Title",
                        )
                    } else {
                        Text(
                            text = state.title,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 16.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }

                    if (state.isEditing) {
                        NoteTextField(
                            value = state.content,
                            onValueChange = { onIntent(NoteDetailIntent.UpdateContent(it)) },
                            label = "Content",
                            isMultiline = true,
                            maxLines = 10,
                        )
                    } else {
                        if (state.content.isNotBlank()) {
                            Text(
                                text = state.content,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 16.dp),
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }

                    state.note?.let { note ->
                        if (!state.isEditing) {
                            NoteMetadataCard(
                                createdAtFormatted = note.createdAtFormatted,
                                updatedAtFormatted = note.updatedAtFormatted,
                            )
                        }
                    }

                    if (state.isEditing && state.note != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(
                            onClick = { onIntent(NoteDetailIntent.CancelEditing) },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }
}

// ---- Previews ----

private val previewNote =
    NoteDetailUiModel(
        id = 1L,
        title = "Sample Note Title",
        content = "This is sample content for the note item to demonstrate how it looks in the theme.",
        createdAtFormatted = "1 day ago",
        updatedAtFormatted = "1 hour ago",
    )

@DualThemePreview
@Composable
private fun NoteDetailContentPreview() {
    PreviewContainer {
        NoteDetailContent(
            state =
                NoteDetailState(
                    note = previewNote,
                    title = previewNote.title,
                    content = previewNote.content,
                ),
            onIntent = {},
        )
    }
}

@DualThemePreview
@Composable
private fun NoteDetailContentEditModePreview() {
    PreviewContainer {
        NoteDetailContent(
            state =
                NoteDetailState(
                    note = previewNote,
                    title = previewNote.title,
                    content = previewNote.content,
                    isEditing = true,
                ),
            onIntent = {},
        )
    }
}

@DualThemePreview
@Composable
private fun NoteDetailContentNewNotePreview() {
    PreviewContainer {
        NoteDetailContent(
            state =
                NoteDetailState(
                    note = null,
                    title = "",
                    content = "",
                    isEditing = true,
                ),
            onIntent = {},
        )
    }
}

@DualThemePreview
@Composable
private fun NoteDetailContentLoadingPreview() {
    PreviewContainer {
        NoteDetailContent(
            state =
                NoteDetailState(
                    isLoading = true,
                ),
            onIntent = {},
        )
    }
}
