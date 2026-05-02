package com.example.android.playground.interappcomm.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.interappcomm.domain.model.IpcChannel
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.presentation.intent.InterAppCommHomeIntent
import com.example.android.playground.interappcomm.presentation.state.InterAppCommHomeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InterAppCommHomeContent(
    state: InterAppCommHomeState,
    onIntent: (InterAppCommHomeIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Inter-App Communication",
                onNavigationClick = { onIntent(InterAppCommHomeIntent.NavigateBack) },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
    ) { innerPadding ->
        when {
            state.isLoading -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Two flavors of this app can talk to each other via five IPC mechanisms. Install both APKs, then pick a channel below.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    item {
                        AppSignatureCard(
                            currentPackage = state.currentPackage,
                            currentFingerprint = state.currentAppSignature,
                            targetPackage = state.targetPackage,
                            targetFingerprint = state.otherAppSignature,
                            signaturesMatch = state.signaturesMatch,
                        )
                    }
                    items(items = state.ipcChannels, key = { it.method.name }) { channel ->
                        IpcMethodCard(
                            channel = channel,
                            onClick = { onIntent(InterAppCommHomeIntent.OnChannelClicked(channel.method)) },
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun InterAppCommHomeContentPreview() {
    PreviewContainer {
        InterAppCommHomeContent(
            state =
                InterAppCommHomeState(
                    isLoading = false,
                    currentPackage = "com.example.android.playground",
                    targetPackage = "com.example.android.playground.variant",
                    currentAppSignature = "AA:BB:CC:DD",
                    otherAppSignature = "AA:BB:CC:DD",
                    signaturesMatch = true,
                    ipcChannels =
                        listOf(
                            IpcChannel(
                                method = IpcMethod.EXPLICIT_INTENT,
                                title = "Explicit Intent",
                                tagline = "Launch the other app with a specific intent.",
                                syncAsync = "Async",
                                dataStyle = "Unstructured",
                                securityLabel = "PackageManager visibility",
                                useCases = listOf("Deep links", "App launch"),
                            ),
                        ),
                ),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}
