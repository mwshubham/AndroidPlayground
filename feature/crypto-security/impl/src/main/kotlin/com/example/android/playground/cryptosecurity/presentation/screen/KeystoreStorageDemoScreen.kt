package com.example.android.playground.cryptosecurity.presentation.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.cryptosecurity.presentation.component.KeystoreStorageDemoContent
import com.example.android.playground.cryptosecurity.presentation.sideeffect.StorageDemoSideEffect
import com.example.android.playground.cryptosecurity.presentation.viewmodel.KeystoreStorageDemoViewModel

@Composable
fun KeystoreStorageDemoScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    viewModel: KeystoreStorageDemoViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    TrackScreenViewEvent(screenName = "KeystoreStorageDemoScreen")

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is StorageDemoSideEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                StorageDemoSideEffect.SavedSuccessfully -> snackbarHostState.showSnackbar("Saved successfully!")
                StorageDemoSideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    KeystoreStorageDemoContent(
        state = state,
        onIntent = viewModel::handleIntent,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}
