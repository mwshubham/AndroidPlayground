package com.example.android.playground.tictactoe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.tictactoe.api.TicTacToeGameRoute
import com.example.android.playground.tictactoe.domain.model.GameMode
import com.example.android.playground.tictactoe.domain.usecase.GetGameHistoryUseCase
import com.example.android.playground.tictactoe.presentation.intent.TicTacToeSetupIntent
import com.example.android.playground.tictactoe.presentation.sideeffect.TicTacToeSetupSideEffect
import com.example.android.playground.tictactoe.presentation.state.TicTacToeSetupState
import com.example.android.playground.tictactoe.util.TicTacToeConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TicTacToeSetupViewModel
    @Inject
    constructor(
        private val getGameHistoryUseCase: GetGameHistoryUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(TicTacToeSetupState())
        val state: StateFlow<TicTacToeSetupState> = _state.asStateFlow()

        private val _sideEffect = Channel<TicTacToeSetupSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        init {
            observeGameHistory()
        }

        fun handleIntent(intent: TicTacToeSetupIntent) {
            when (intent) {
                is TicTacToeSetupIntent.OnModeSelected -> onModeSelected(intent.mode)
                is TicTacToeSetupIntent.OnPlayer1NameChanged -> onPlayer1NameChanged(intent.name)
                is TicTacToeSetupIntent.OnPlayer2NameChanged -> onPlayer2NameChanged(intent.name)
                TicTacToeSetupIntent.OnStartGameTapped -> onStartGameTapped()
                TicTacToeSetupIntent.OnNavigateBack -> sendEffect(TicTacToeSetupSideEffect.NavigateBack)
            }
        }

        private fun observeGameHistory() {
            getGameHistoryUseCase()
                .onEach { results ->
                    _state.update { it.copy(gamesPlayed = results.size) }
                }.launchIn(viewModelScope)
        }

        private fun onModeSelected(mode: GameMode) {
            _state.update { current ->
                val player2Name = if (mode is GameMode.PlayerVsAi) TicTacToeConstants.AI_PLAYER_NAME else current.player2Name
                current.copy(
                    selectedMode = mode,
                    player2Name = player2Name,
                    isStartEnabled = isStartEnabled(current.player1Name, player2Name),
                )
            }
        }

        private fun onPlayer1NameChanged(name: String) {
            _state.update { current ->
                current.copy(
                    player1Name = name,
                    isStartEnabled = isStartEnabled(name, current.player2Name),
                )
            }
        }

        private fun onPlayer2NameChanged(name: String) {
            _state.update { current ->
                current.copy(
                    player2Name = name,
                    isStartEnabled = isStartEnabled(current.player1Name, name),
                )
            }
        }

        private fun onStartGameTapped() {
            val state = _state.value
            if (!state.isStartEnabled) return

            Timber.d("Starting game: mode=${state.selectedMode}, p1=${state.player1Name}, p2=${state.player2Name}")

            val modeString =
                when (state.selectedMode) {
                    GameMode.PlayerVsPlayer -> TicTacToeConstants.MODE_PVP
                    GameMode.PlayerVsAi -> TicTacToeConstants.MODE_PVA
                }
            sendEffect(
                TicTacToeSetupSideEffect.NavigateToGame(
                    TicTacToeGameRoute(
                        mode = modeString,
                        player1Name = state.player1Name,
                        player2Name = state.player2Name,
                    ),
                ),
            )
        }

        private fun isStartEnabled(
            player1Name: String,
            player2Name: String,
        ): Boolean = player1Name.isNotBlank() && player2Name.isNotBlank()

        private fun sendEffect(effect: TicTacToeSetupSideEffect) {
            viewModelScope.launch { _sideEffect.send(effect) }
        }
    }
