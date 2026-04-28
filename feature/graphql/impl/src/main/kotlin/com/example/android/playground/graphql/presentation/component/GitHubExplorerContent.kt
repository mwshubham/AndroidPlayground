package com.example.android.playground.graphql.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.graphql.presentation.intent.GitHubExplorerIntent
import com.example.android.playground.graphql.presentation.model.DataSourceMode
import com.example.android.playground.graphql.presentation.model.RepoUiModel
import com.example.android.playground.graphql.presentation.state.GitHubExplorerState

@Composable
internal fun GitHubExplorerContent(
    state: GitHubExplorerState,
    onIntent: (GitHubExplorerIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var tokenVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            onIntent(GitHubExplorerIntent.OnDismissError)
        }
    }

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "GitHub GraphQL Explorer",
                onNavigationClick = { onIntent(GitHubExplorerIntent.NavigateBack) },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier,
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            // ── Token section ───────────────────────────────────────────
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "GitHub Personal Access Token",
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = "Generate one at github.com/settings/tokens (read:user + public_repo scopes)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.token,
                    onValueChange = { onIntent(GitHubExplorerIntent.OnTokenChanged(it)) },
                    label = { Text("ghp_…") },
                    singleLine = true,
                    visualTransformation = if (tokenVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(onDone = { onIntent(GitHubExplorerIntent.OnSaveToken) }),
                    trailingIcon = {
                        IconButton(onClick = { tokenVisible = !tokenVisible }) {
                            Icon(
                                imageVector = if (tokenVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (tokenVisible) "Hide token" else "Show token",
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { onIntent(GitHubExplorerIntent.OnSaveToken) },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(if (state.isTokenSaved) "Update Token" else "Save Token")
                    }
                    if (state.isTokenSaved) {
                        OutlinedButton(
                            onClick = { onIntent(GitHubExplorerIntent.OnClearToken) },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("Clear Token")
                        }
                    }
                }
            }

            // ── Search section ──────────────────────────────────────────
            item {
                Spacer(Modifier.height(16.dp))

                // ── Data source toggle ──────────────────────────────────
                // Switch between Raw OkHttp and Apollo Kotlin to run the
                // same query with two different implementations and compare.
                Text(
                    text = "Data Source",
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(Modifier.height(6.dp))
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    DataSourceMode.entries.forEachIndexed { index, mode ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = DataSourceMode.entries.size,
                            ),
                            selected = state.mode == mode,
                            onClick = { onIntent(GitHubExplorerIntent.OnModeChanged(mode)) },
                        ) {
                            Text(mode.label)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Search Repositories",
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { onIntent(GitHubExplorerIntent.OnSearchQueryChanged(it)) },
                    label = { Text("e.g. android kotlin stars:>500") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onIntent(GitHubExplorerIntent.OnSearch) }),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (state.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onIntent(GitHubExplorerIntent.OnSearchQueryChanged("")) }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear query")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { onIntent(GitHubExplorerIntent.OnSearch) },
                    enabled = state.searchQuery.isNotBlank() && !state.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Search")
                }
            }

            // ── Loading indicator ───────────────────────────────────────
            if (state.isLoading) {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            // ── Results header ──────────────────────────────────────────
            if (state.repos.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "${state.totalCount} repositories  ·  via ${state.mode.label}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // ── Repo cards ──────────────────────────────────────────────
            itemsIndexed(state.repos, key = { _, item -> item.nameWithOwner }) { index, repo ->
                RepoCard(
                    repo = repo,
                    onClick = { onIntent(GitHubExplorerIntent.OnRepoClicked(repo.url)) },
                )
                // Trigger load-more when the user reaches the last 5 items.
                if (index == state.repos.size - 5 && state.hasNextPage && !state.isLoadingMore) {
                    onIntent(GitHubExplorerIntent.OnLoadMore)
                }
            }

            // ── Load-more indicator ─────────────────────────────────────
            if (state.isLoadingMore) {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            Text("Loading more…", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(88.dp)) }
        }
    }
}

// ── Previews ────────────────────────────────────────────────────────────────

@ComponentPreview
@Composable
private fun GitHubExplorerContentEmptyPreview() {
    PreviewContainer {
        GitHubExplorerContent(
            state = GitHubExplorerState(),
            onIntent = {},
        )
    }
}

@ComponentPreview
@Composable
private fun GitHubExplorerContentWithResultsPreview() {
    PreviewContainer {
        GitHubExplorerContent(
            state = GitHubExplorerState(
                isTokenSaved = true,
                token = "ghp_example",
                searchQuery = "android kotlin",
                totalCount = 1500,
                repos = listOf(
                    RepoUiModel(
                        name = "architecture-samples",
                        nameWithOwner = "android/architecture-samples",
                        description = "A collection of samples to discuss and showcase different architectural tools.",
                        starsDisplay = "44.2k",
                        language = "Kotlin",
                        languageColor = "#A97BFF",
                        url = "https://github.com/android/architecture-samples",
                        ownerLogin = "android",
                    ),
                    RepoUiModel(
                        name = "compose-samples",
                        nameWithOwner = "android/compose-samples",
                        description = "Official Jetpack Compose samples.",
                        starsDisplay = "20.1k",
                        language = "Kotlin",
                        languageColor = "#A97BFF",
                        url = "https://github.com/android/compose-samples",
                        ownerLogin = "android",
                    ),
                ),
            ),
            onIntent = {},
        )
    }
}
