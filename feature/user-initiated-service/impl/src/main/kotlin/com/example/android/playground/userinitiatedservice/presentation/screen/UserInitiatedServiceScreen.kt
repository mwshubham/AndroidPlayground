package com.example.android.playground.userinitiatedservice.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.userinitiatedservice.presentation.component.ApiLevelBanner
import com.example.android.playground.userinitiatedservice.presentation.component.ConceptCard
import com.example.android.playground.userinitiatedservice.presentation.component.TransferItemCard
import com.example.android.playground.userinitiatedservice.presentation.intent.UserInitiatedServiceIntent
import com.example.android.playground.userinitiatedservice.presentation.sideeffect.UserInitiatedServiceSideEffect
import com.example.android.playground.userinitiatedservice.presentation.state.JobStatus
import com.example.android.playground.userinitiatedservice.presentation.state.UserInitiatedServiceState
import com.example.android.playground.userinitiatedservice.presentation.viewmodel.UserInitiatedServiceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInitiatedServiceScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    viewModel: UserInitiatedServiceViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    TrackScreenViewEvent(screenName = "UserInitiatedServiceScreen")

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is UserInitiatedServiceSideEffect.NavigateBack -> onNavigateBack()
                is UserInitiatedServiceSideEffect.ShowMessage ->
                    snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "User-Initiated Transfer",
                onNavigationClick = {
                    viewModel.handleIntent(UserInitiatedServiceIntent.NavigateBack)
                },
                actions = {
                    if (state.items.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                viewModel.handleIntent(UserInitiatedServiceIntent.ClearAll)
                            },
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
                    // UIJ does not need POST_NOTIFICATIONS — it surfaces in the Task Manager,
                    // not in the notification shade. No permission launcher required here.
                    if (state.jobStatus != JobStatus.RUNNING) {
                        viewModel.handleIntent(UserInitiatedServiceIntent.StartTransfer)
                    }
                },
                containerColor =
                    if (state.jobStatus == JobStatus.RUNNING) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Start Transfer",
                    tint =
                        if (state.jobStatus == JobStatus.RUNNING) {
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
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp),
        ) {
            item {
                ApiLevelBanner(isApi34Plus = state.isApi34Plus)
            }
            item {
                ConceptCard()
            }

            if (state.totalCount > 0) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    SummaryRow(state = state)
                }
                item {
                    LinearProgressIndicator(
                        progress = { state.overallProgress },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                items(items = state.items, key = { it.id }) { item ->
                    TransferItemCard(item = item)
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(
    state: UserInitiatedServiceState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        SummaryLabel("Total", state.totalCount.toString())
        SummaryLabel("Running", state.runningCount.toString())
        SummaryLabel("Done", state.successCount.toString())
        SummaryLabel("Failed", state.failedCount.toString())
        SummaryLabel("Cancelled", state.cancelledCount.toString())
        SummaryLabel("Queued", state.pendingCount.toString())
    }
}

@Composable
private fun SummaryLabel(
    label: String,
    value: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleMedium)
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
