package com.example.android.playground.interappcomm.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.domain.usecase.CheckOtherAppInstalledUseCase
import com.example.android.playground.interappcomm.domain.usecase.GetAppSignatureUseCase
import com.example.android.playground.interappcomm.domain.usecase.GetIpcChannelsUseCase
import com.example.android.playground.interappcomm.presentation.intent.InterAppCommHomeIntent
import com.example.android.playground.interappcomm.presentation.sideeffect.InterAppCommHomeSideEffect
import com.example.android.playground.interappcomm.presentation.state.InterAppCommHomeState
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
class InterAppCommHomeViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val checkOtherAppInstalled: CheckOtherAppInstalledUseCase,
    private val getAppSignature: GetAppSignatureUseCase,
    private val getIpcChannels: GetIpcChannelsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(InterAppCommHomeState())
    val state: StateFlow<InterAppCommHomeState> = _state.asStateFlow()

    private val _sideEffect = Channel<InterAppCommHomeSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        handleIntent(InterAppCommHomeIntent.LoadData)
    }

    fun handleIntent(intent: InterAppCommHomeIntent) {
        when (intent) {
            InterAppCommHomeIntent.LoadData -> loadData()
            is InterAppCommHomeIntent.OnChannelClicked -> navigateToChannel(intent.method)
            InterAppCommHomeIntent.NavigateBack -> viewModelScope.launch {
                _sideEffect.send(InterAppCommHomeSideEffect.NavigateBack)
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val currentPkg = context.packageName
            val targetPkg = InterAppCommConstants.getTargetPackage(currentPkg)
            val isInstalled = checkOtherAppInstalled(currentPkg)
            val currentSig = getAppSignature(currentPkg)
            val otherSig = if (isInstalled) getAppSignature(targetPkg) else null
            val channels = getIpcChannels()
            _state.update {
                it.copy(
                    isLoading = false,
                    currentPackage = currentPkg,
                    targetPackage = targetPkg,
                    isOtherAppInstalled = isInstalled,
                    currentAppSignature = currentSig,
                    otherAppSignature = otherSig,
                    signaturesMatch = currentSig != null && currentSig == otherSig,
                    ipcChannels = channels,
                )
            }
        }
    }

    private fun navigateToChannel(method: IpcMethod) {
        viewModelScope.launch {
            _sideEffect.send(InterAppCommHomeSideEffect.NavigateToChannel(method))
        }
    }
}
