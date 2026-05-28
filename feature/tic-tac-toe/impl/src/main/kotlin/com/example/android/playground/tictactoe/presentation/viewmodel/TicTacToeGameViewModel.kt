package com.example.android.playground.tictactoe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.tictactoe.api.TicTacToeGameRoute
import com.example.android.playground.tictactoe.domain.model.GameMode
import com.example.android.playground.tictactoe.domain.model.GameResult
import com.example.android.playground.tictactoe.domain.model.GameStatus
import com.example.android.playground.tictactoe.domain.usecase.GetAiMoveUseCase
import com.example.android.playground.tictactoe.domain.usecase.MakeMoveUseCase
import com.example.android.playground.tictactoe.domain.usecase.ResetGameUseCase
import com.example.android.playground.tictactoe.domain.usecase.SaveGameResultUseCase
import com.example.android.playground.tictactoe.domain.usecase.StartGameUseCase
import com.example.android.playground.tictactoe.presentation.intent.TicTacToeGameIntent
import com.example.android.playground.tictactoe.presentation.sideeffect.TicTacToeGameSideEffect
import com.example.android.playground.tictactoe.presentation.state.TicTacToeGameState
import com.example.android.playground.tictactoe.util.TicTacToeConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TicTacToeGameViewModel
    @Inject
    constructor(
        private val startGameUseCase: StartGameUseCase,
        private val makeMoveUseCase: MakeMoveUseCase,
        private val getAiMoveUseCase: GetAiMoveUseCase,
        private val resetGameUseCase: ResetGameUseCase,
        private val saveGameResultUseCase: SaveGameResultUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(TicTacToeGameState())
        val state: StateFlow<TicTacToeGameState> = _state.asStateFlow()

        private val _sideEffect = Channel<TicTacToeGameSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        fun initGame(route: TicTacToeGameRoute) {
            val mode = if (route.mode == TicTacToeConstants.MODE_PVP) GameMode.PlayerVsPlayer else GameMode.PlayerVsAi
            val game = startGameUseCase(mode, route.player1Name, route.player2Name)
            Timber.d("Game initialised: mode=${route.mode}, p1=${route.player1Name}, p2=${route.player2Name}")
            _state.update { it.copy(game = game) }
        }

        fun handleIntent(intent: TicTacToeGameIntent) {
            when (intent) {
                is TicTacToeGameIntent.OnCellTapped -> onCellTapped(intent.row, intent.col)
                TicTacToeGameIntent.OnResetGameTapped -> onResetGame()
                TicTacToeGameIntent.OnNavigateBack -> sendEffect(TicTacToeGameSideEffect.NavigateBack)
            }
        }

        private fun onCellTapped(
            row: Int,
            col: Int,
        ) {
            val current = _state.value
            if (current.isAiThinking) return
            if (current.status != GameStatus.InProgress) return

            val updated = makeMoveUseCase(current.game, row, col)
            _state.update { it.copy(game = updated) }

            when (val status = updated.status) {
                is GameStatus.Won -> saveResult(updated.mode, status.winner.name, updated.moveCount)
                GameStatus.Draw -> saveResult(updated.mode, null, updated.moveCount)
                GameStatus.InProgress -> {
                    if (updated.mode is GameMode.PlayerVsAi) triggerAiMove()
                }
            }
        }

        private fun triggerAiMove() {
            viewModelScope.launch {
                _state.update { it.copy(isAiThinking = true) }
                delay(TicTacToeConstants.AI_MOVE_DELAY_MS)

                val current = _state.value.game
                if (current.status != GameStatus.InProgress) {
                    _state.update { it.copy(isAiThinking = false) }
                    return@launch
                }

                val (row, col) = getAiMoveUseCase(current.board, current.currentPlayer)
                val updated = makeMoveUseCase(current, row, col)
                _state.update { it.copy(game = updated, isAiThinking = false) }

                when (val status = updated.status) {
                    is GameStatus.Won -> saveResult(updated.mode, status.winner.name, updated.moveCount)
                    GameStatus.Draw -> saveResult(updated.mode, null, updated.moveCount)
                    GameStatus.InProgress -> Unit
                }
            }
        }

        private fun onResetGame() {
            val reset = resetGameUseCase(_state.value.game)
            _state.update { it.copy(game = reset, isAiThinking = false) }
        }

        private fun saveResult(
            mode: GameMode,
            winnerName: String?,
            totalMoves: Int,
        ) {
            val modeString = if (mode is GameMode.PlayerVsPlayer) TicTacToeConstants.MODE_PVP else TicTacToeConstants.MODE_PVA
            viewModelScope.launch {
                saveGameResultUseCase(
                    GameResult(
                        winnerName = winnerName,
                        mode = modeString,
                        totalMoves = totalMoves,
                        playedAt = System.currentTimeMillis(),
                    ),
                )
            }
        }

        private fun sendEffect(effect: TicTacToeGameSideEffect) {
            viewModelScope.launch { _sideEffect.send(effect) }
        }
    }
