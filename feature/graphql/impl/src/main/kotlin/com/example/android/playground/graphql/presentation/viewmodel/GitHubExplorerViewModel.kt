package com.example.android.playground.graphql.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.graphql.domain.usecase.ApolloSearchReposUseCase
import com.example.android.playground.graphql.domain.usecase.GetSavedTokenUseCase
import com.example.android.playground.graphql.domain.usecase.SaveTokenUseCase
import com.example.android.playground.graphql.domain.usecase.RawSearchReposUseCase
import com.example.android.playground.graphql.presentation.intent.GitHubExplorerIntent
import com.example.android.playground.graphql.presentation.mapper.toUiModel
import com.example.android.playground.graphql.presentation.model.DataSourceMode
import com.example.android.playground.graphql.presentation.sideeffect.GitHubExplorerSideEffect
import com.example.android.playground.graphql.presentation.state.GitHubExplorerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GitHubExplorerViewModel @Inject constructor(
    private val rawSearchReposUseCase: RawSearchReposUseCase,
    private val apolloSearchReposUseCase: ApolloSearchReposUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val getSavedTokenUseCase: GetSavedTokenUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(GitHubExplorerState())
    val state: StateFlow<GitHubExplorerState> = _state.asStateFlow()

    private val _sideEffect = Channel<GitHubExplorerSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        observeSavedToken()
    }

    fun handleIntent(intent: GitHubExplorerIntent) {
        when (intent) {
            is GitHubExplorerIntent.OnModeChanged -> onModeChanged(intent.mode)
            is GitHubExplorerIntent.OnTokenChanged -> onTokenChanged(intent.token)
            is GitHubExplorerIntent.OnSaveToken -> saveToken()
            is GitHubExplorerIntent.OnClearToken -> clearToken()
            is GitHubExplorerIntent.OnSearchQueryChanged -> onSearchQueryChanged(intent.query)
            is GitHubExplorerIntent.OnSearch -> search()
            is GitHubExplorerIntent.OnLoadMore -> loadMore()
            is GitHubExplorerIntent.OnRepoClicked -> openRepo(intent.url)
            is GitHubExplorerIntent.OnDismissError -> dismissError()
            is GitHubExplorerIntent.NavigateBack -> navigateBack()
        }
    }

    private fun observeSavedToken() {
        viewModelScope.launch {
            getSavedTokenUseCase().collect { savedToken ->
                _state.update { it.copy(token = savedToken, isTokenSaved = savedToken.isNotBlank()) }
            }
        }
    }

    private fun onModeChanged(mode: DataSourceMode) {
        // Clear results when switching modes so the user can see a fresh comparison.
        _state.update { it.copy(mode = mode, repos = emptyList(), totalCount = 0, hasNextPage = false, endCursor = null, error = null) }
    }

    private fun onTokenChanged(token: String) {
        _state.update { it.copy(token = token) }
    }

    private fun saveToken() {
        val token = _state.value.token.trim()
        if (token.isBlank()) {
            _state.update { it.copy(error = "Token cannot be empty.") }
            return
        }
        viewModelScope.launch {
            saveTokenUseCase(token)
            _sideEffect.send(GitHubExplorerSideEffect.ShowMessage("Token saved successfully."))
        }
    }

    private fun clearToken() {
        viewModelScope.launch {
            saveTokenUseCase("")
            _state.update { it.copy(repos = emptyList(), totalCount = 0, hasNextPage = false, endCursor = null) }
        }
    }

    private fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    private fun search() {
        val current = _state.value
        if (current.searchQuery.isBlank()) return
        if (!current.isTokenSaved) {
            _state.update { it.copy(error = "Please save a GitHub PAT token first.") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, repos = emptyList(), endCursor = null) }
            runCatching {
                when (current.mode) {
                    DataSourceMode.RAW_OKHTTP -> rawSearchReposUseCase(
                        query = current.searchQuery.trim(),
                        token = current.token,
                    )
                    DataSourceMode.APOLLO -> apolloSearchReposUseCase(
                        query = current.searchQuery.trim(),
                        token = current.token,
                    )
                }
            }.onSuccess { result ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        repos = result.repos.map { repo -> repo.toUiModel() },
                        totalCount = result.totalCount,
                        hasNextPage = result.hasNextPage,
                        endCursor = result.endCursor,
                    )
                }
            }.onFailure { throwable ->
                _state.update { it.copy(isLoading = false, error = throwable.message ?: "An error occurred.") }
            }
        }
    }

    private fun loadMore() {
        val current = _state.value
        if (!current.hasNextPage || current.isLoadingMore) return
        viewModelScope.launch {
            _state.update { it.copy(isLoadingMore = true) }
            runCatching {
                when (current.mode) {
                    DataSourceMode.RAW_OKHTTP -> rawSearchReposUseCase(
                        query = current.searchQuery.trim(),
                        token = current.token,
                        after = current.endCursor,
                    )
                    DataSourceMode.APOLLO -> apolloSearchReposUseCase(
                        query = current.searchQuery.trim(),
                        token = current.token,
                        after = current.endCursor,
                    )
                }
            }.onSuccess { result ->
                _state.update {
                    it.copy(
                        isLoadingMore = false,
                        repos = it.repos + result.repos.map { repo -> repo.toUiModel() },
                        hasNextPage = result.hasNextPage,
                        endCursor = result.endCursor,
                    )
                }
            }.onFailure { throwable ->
                _state.update { it.copy(isLoadingMore = false, error = throwable.message ?: "Failed to load more.") }
            }
        }
    }

    private fun openRepo(url: String) {
        viewModelScope.launch {
            _sideEffect.send(GitHubExplorerSideEffect.OpenUrl(url))
        }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _sideEffect.send(GitHubExplorerSideEffect.NavigateBack)
        }
    }
}
