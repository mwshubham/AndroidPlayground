package com.example.android.playground.interappcomm.presentation.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.interappcomm.domain.usecase.CheckOtherAppInstalledUseCase
import com.example.android.playground.interappcomm.presentation.intent.ExplicitIntentIntent
import com.example.android.playground.interappcomm.presentation.sideeffect.ExplicitIntentSideEffect
import com.example.android.playground.interappcomm.presentation.state.ExplicitIntentState
import com.example.android.playground.interappcomm.util.InterAppCommConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExplicitIntentViewModel
    @Inject
    constructor(
        @param:ApplicationContext private val context: Context,
        private val checkOtherAppInstalled: CheckOtherAppInstalledUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(ExplicitIntentState())
        val state: StateFlow<ExplicitIntentState> = _state.asStateFlow()

        private val _sideEffect = Channel<ExplicitIntentSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        init {
            handleIntent(ExplicitIntentIntent.LoadData)
        }

        fun handleIntent(intent: ExplicitIntentIntent) {
            when (intent) {
                ExplicitIntentIntent.LoadData -> loadData()
                ExplicitIntentIntent.LaunchOtherApp -> launchOtherApp()
                ExplicitIntentIntent.NavigateBack ->
                    viewModelScope.launch {
                        _sideEffect.send(ExplicitIntentSideEffect.NavigateBack)
                    }
            }
        }

        private fun loadData() {
            viewModelScope.launch {
                val currentPkg = context.packageName
                val targetPkg = InterAppCommConstants.getTargetPackage(currentPkg)
                _state.update {
                    it.copy(
                        currentPackage = currentPkg,
                        targetPackage = targetPkg,
                        isOtherAppInstalled = checkOtherAppInstalled(currentPkg),
                    )
                }
            }
        }

        private fun launchOtherApp() {
            viewModelScope.launch {
                val targetPkg = _state.value.targetPackage
                // getLaunchIntentForPackage returns null if the package is not installed
                // or not visible (API 30+ requires <queries> in the manifest).
                val launchIntent = context.packageManager.getLaunchIntentForPackage(targetPkg)
                if (launchIntent == null) {
                    _sideEffect.send(
                        ExplicitIntentSideEffect.ShowMessage(
                            "App not found: $targetPkg. Install the other flavor first.",
                        ),
                    )
                    return@launch
                }
                // Add a custom extra to demonstrate data passing via Explicit Intent
                launchIntent.putExtra("launched_from", context.packageName)
                launchIntent.putExtra("message", "Hello from ${context.packageName}!")
                // FLAG_ACTIVITY_NEW_TASK is required when starting from a non-Activity context
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                _sideEffect.send(ExplicitIntentSideEffect.LaunchIntent(launchIntent))
                _state.update {
                    it.copy(lastLaunchResult = "Launched $targetPkg successfully")
                }
            }
        }
    }
