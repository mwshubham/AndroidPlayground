package com.example.android.playground.mediaorchestrator.presentation.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.mediaorchestrator.presentation.component.MediaOrchestratorContent
import com.example.android.playground.mediaorchestrator.presentation.intent.MediaOrchestratorIntent
import com.example.android.playground.mediaorchestrator.presentation.sideeffect.MediaOrchestratorSideEffect
import com.example.android.playground.mediaorchestrator.presentation.state.WorkerStatus
import com.example.android.playground.mediaorchestrator.presentation.viewmodel.MediaOrchestratorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaOrchestratorScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    viewModel: MediaOrchestratorViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // On API 33+, POST_NOTIFICATIONS is a runtime permission required to show the
    // foreground service notification while the upload runs in the background.
    // We request it here before the user taps Pick & Upload so the notification is
    // visible when the worker promotes to a foreground service.
    // On API < 33 notifications are always allowed — no runtime permission needed.
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        // Permission result received — proceed to enqueue regardless.
        // If denied: worker still runs, foreground service still starts,
        // the notification is just silently suppressed by the OS.
        viewModel.handleIntent(MediaOrchestratorIntent.PickAndEnqueue)
    }

    TrackScreenViewEvent(screenName = "MediaOrchestratorScreen")

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is MediaOrchestratorSideEffect.NavigateBack -> onNavigateBack()
                is MediaOrchestratorSideEffect.ShowMessage ->
                    snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Media Orchestrator",
                onNavigationClick = { viewModel.handleIntent(MediaOrchestratorIntent.NavigateBack) },
                actions = {
                    if (state.items.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.handleIntent(MediaOrchestratorIntent.ClearAll) },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Clear all",
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (state.workerStatus != WorkerStatus.RUNNING) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            // Request POST_NOTIFICATIONS on API 33+.
                            // The launcher callback calls PickAndEnqueue after the result,
                            // so the upload starts regardless of whether the user grants or denies.
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            // API < 33: notifications always permitted, enqueue immediately.
                            viewModel.handleIntent(MediaOrchestratorIntent.PickAndEnqueue)
                        }
                    }
                },
                containerColor = if (state.workerStatus == WorkerStatus.RUNNING) {
                    MaterialTheme.colorScheme.surfaceVariant
                } else {
                    MaterialTheme.colorScheme.primary
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Pick & Upload",
                    tint = if (state.workerStatus == WorkerStatus.RUNNING) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    },
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
    ) { paddingValues ->
        MediaOrchestratorContent(
            state = state,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

