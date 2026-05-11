package com.example.android.playground.note.presentation.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.note.presentation.component.NoteListContent
import com.example.android.playground.note.presentation.intent.NoteListIntent
import com.example.android.playground.note.presentation.sideeffect.NoteListSideEffect
import com.example.android.playground.note.presentation.viewmodel.NoteListViewModel
import com.example.android.playground.note.util.NoteConstants

@Composable
fun NoteListScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
    viewModel: NoteListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
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

    NoteListContent(
        modifier = modifier,
        state = state,
        snackbarHostState = snackbarHostState,
        onNavigateBack = onNavigateBack,
        onAddClick = { viewModel.handleIntent(NoteListIntent.NavigateToAdd) },
        onNoteClick = { noteId -> viewModel.handleIntent(NoteListIntent.NavigateToDetail(noteId)) },
        onSearchQueryChange = { query -> viewModel.handleIntent(NoteListIntent.SearchNotes(query)) },
        onDeleteNote = { noteId -> viewModel.handleIntent(NoteListIntent.DeleteNote(noteId)) },
        onErrorDismiss = { viewModel.handleIntent(NoteListIntent.ClearError) },
    )

    TrackScreenViewEvent(screenName = NoteConstants.NOTE_LIST_SCREEN_NAME)
}
