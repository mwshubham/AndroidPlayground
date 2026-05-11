package com.example.android.playground.note.presentation.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.note.api.NoteDetailRoute
import com.example.android.playground.note.presentation.component.NoteDetailContent
import com.example.android.playground.note.presentation.intent.NoteDetailIntent
import com.example.android.playground.note.presentation.sideeffect.NoteDetailSideEffect
import com.example.android.playground.note.presentation.viewmodel.NoteDetailViewModel
import com.example.android.playground.note.util.NoteConstants
import kotlinx.coroutines.launch

@Composable
fun NoteDetailScreen(
    route: NoteDetailRoute,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: NoteDetailViewModel =
        hiltViewModel(
            creationCallback = { factory: NoteDetailViewModel.Factory ->
                factory.create(noteId = route.noteId)
            },
        ),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is NoteDetailSideEffect.NavigateBack -> onNavigateBack()
                is NoteDetailSideEffect.ShowSuccessMessage -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(sideEffect.message)
                    }
                }
                is NoteDetailSideEffect.ShowErrorMessage -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(sideEffect.message)
                    }
                }
            }
        }
    }

    NoteDetailContent(
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
        onErrorDismiss = { viewModel.handleIntent(NoteDetailIntent.ClearError) },
    )

    TrackScreenViewEvent(screenName = NoteConstants.NOTE_DETAIL_SCREEN_NAME)
}
