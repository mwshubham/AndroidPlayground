package com.example.android.playground.note.presentation.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.note.api.NoteDetailRoute
import com.example.android.playground.note.presentation.component.NoteDetailContent
import com.example.android.playground.note.presentation.sideeffect.NoteDetailSideEffect
import com.example.android.playground.note.presentation.viewmodel.NoteDetailViewModel
import com.example.android.playground.note.util.NoteConstants

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

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is NoteDetailSideEffect.NavigateBack -> onNavigateBack()
                is NoteDetailSideEffect.ShowSuccessMessage -> snackbarHostState.showSnackbar(sideEffect.message)
                is NoteDetailSideEffect.ShowErrorMessage -> snackbarHostState.showSnackbar(sideEffect.message)
            }
        }
    }

    NoteDetailContent(
        state = state,
        onIntent = viewModel::handleIntent,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )

    TrackScreenViewEvent(screenName = NoteConstants.NOTE_DETAIL_SCREEN_NAME)
}
