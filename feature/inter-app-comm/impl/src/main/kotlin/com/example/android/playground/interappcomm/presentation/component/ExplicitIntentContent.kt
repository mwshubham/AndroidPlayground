package com.example.android.playground.interappcomm.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.example.android.playground.interappcomm.presentation.intent.ExplicitIntentIntent
import com.example.android.playground.interappcomm.presentation.state.ExplicitIntentState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExplicitIntentContent(
    state: ExplicitIntentState,
    onIntent: (ExplicitIntentIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Explicit Intent",
                onNavigationClick = { onIntent(ExplicitIntentIntent.NavigateBack) },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) { CircularProgressIndicator() }
        } else {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SecurityInfoCard(
                    title = "Security: Package Visibility (API 30+)",
                    mechanism = "<queries> block in AndroidManifest.xml",
                    notes =
                        listOf(
                            "Without <queries>, getLaunchIntentForPackage() returns null even if the app is installed.",
                            "No custom permission needed — just declare visibility.",
                            "The target app controls what it exposes via exported=true.",
                        ),
                )
                Text(
                    text = "Current: ${state.currentPackage}",
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = "Target: ${state.targetPackage}",
                    style = MaterialTheme.typography.bodySmall,
                    color =
                        if (state.isOtherAppInstalled) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                )
                if (!state.isOtherAppInstalled) {
                    Text(
                        text = "The other flavor is not installed. Build and install the other variant first.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                state.lastLaunchResult?.let { result ->
                    Text(
                        text = result,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { onIntent(ExplicitIntentIntent.LaunchOtherApp) },
                    enabled = state.isOtherAppInstalled,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Launch other app")
                }
            }
        }
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun ExplicitIntentContentPreview() {
    PreviewContainer {
        ExplicitIntentContent(
            state =
                ExplicitIntentState(
                    currentPackage = "com.example.android.playground",
                    targetPackage = "com.example.android.playground.variant",
                    isOtherAppInstalled = true,
                ),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}
