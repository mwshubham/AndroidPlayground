package com.example.android.playground.graphql.presentation.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.graphql.presentation.component.GitHubExplorerContent
import com.example.android.playground.graphql.presentation.intent.GitHubExplorerIntent
import com.example.android.playground.graphql.presentation.sideeffect.GitHubExplorerSideEffect
import com.example.android.playground.graphql.presentation.viewmodel.GitHubExplorerViewModel
import androidx.core.net.toUri

@Composable
fun GitHubExplorerScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    viewModel: GitHubExplorerViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    TrackScreenViewEvent(screenName = "GitHubExplorerScreen")

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is GitHubExplorerSideEffect.OpenUrl -> {
                    val intent = Intent(Intent.ACTION_VIEW, effect.url.toUri())
                    context.startActivity(intent)
                }
                is GitHubExplorerSideEffect.ShowMessage -> {
                    // Message is also surfaced via state snackbar; no extra handling needed here.
                }
                is GitHubExplorerSideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    GitHubExplorerContent(
        state = state,
        onIntent = viewModel::handleIntent,
        modifier = modifier,
    )
}
