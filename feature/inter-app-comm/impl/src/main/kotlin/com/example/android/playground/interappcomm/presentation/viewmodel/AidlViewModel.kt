package com.example.android.playground.interappcomm.presentation.viewmodel

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.interappcomm.IInterAppService
import com.example.android.playground.interappcomm.domain.model.IpcMessage
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.domain.model.MessageDirection
import com.example.android.playground.interappcomm.presentation.intent.AidlIntent
import com.example.android.playground.interappcomm.presentation.sideeffect.AidlSideEffect
import com.example.android.playground.interappcomm.presentation.state.AidlState
import com.example.android.playground.interappcomm.util.InterAppCommConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AidlViewModel
    @Inject
    constructor(
        @param:ApplicationContext private val context: Context,
    ) : ViewModel() {
        private val _state = MutableStateFlow(AidlState())
        val state: StateFlow<AidlState> = _state.asStateFlow()

        private val _sideEffect = Channel<AidlSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        private var service: IInterAppService? = null

        private val serviceConnection =
            object : ServiceConnection {
                override fun onServiceConnected(
                    name: ComponentName,
                    binder: IBinder,
                ) {
                    // IInterAppService.Stub.asInterface handles both in-process and cross-process cases
                    service = IInterAppService.Stub.asInterface(binder)
                    _state.update { it.copy(isConnected = true, isConnecting = false, error = null) }
                }

                override fun onServiceDisconnected(name: ComponentName) {
                    service = null
                    _state.update { it.copy(isConnected = false, isConnecting = false) }
                }
            }

        init {
            _state.update {
                it.copy(
                    currentPackage = context.packageName,
                    targetPackage = InterAppCommConstants.getTargetPackage(context.packageName),
                )
            }
        }

        fun handleIntent(intent: AidlIntent) {
            when (intent) {
                AidlIntent.Connect -> connect()
                AidlIntent.Disconnect -> disconnect()
                AidlIntent.Ping -> ping()
                AidlIntent.GetMessages -> getMessages()
                is AidlIntent.OnInputChanged -> _state.update { it.copy(inputText = intent.text) }
                AidlIntent.PostMessage -> postMessage()
                AidlIntent.NavigateBack -> {
                    if (_state.value.isConnected) disconnect()
                    viewModelScope.launch { _sideEffect.send(AidlSideEffect.NavigateBack) }
                }
            }
        }

        private fun connect() {
            val targetPackage = _state.value.targetPackage
            val intent =
                Intent().apply {
                    setClassName(targetPackage, InterAppCommConstants.AIDL_SERVICE_CLASS)
                }
            _state.update { it.copy(isConnecting = true, error = null) }
            val bound = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            if (!bound) {
                _state.update {
                    it.copy(isConnecting = false, error = "Could not bind to $targetPackage. Is the other app installed?")
                }
            }
        }

        private fun disconnect() {
            runCatching { context.unbindService(serviceConnection) }
            service = null
            _state.update { it.copy(isConnected = false, isConnecting = false) }
        }

        private fun ping() {
            viewModelScope.launch {
                val svc =
                    service ?: run {
                        _sideEffect.send(AidlSideEffect.ShowMessage("Not connected — bind first"))
                        return@launch
                    }
                try {
                    // ping() is a synchronous AIDL call — must NOT run on the main thread
                    val result =
                        withContext(Dispatchers.IO) {
                            svc.ping("Hello from ${context.packageName}")
                        }
                    _state.update { it.copy(pingResult = result) }
                    val msg =
                        IpcMessage(
                            content = "ping → $result",
                            sender = context.packageName,
                            method = IpcMethod.AIDL,
                            direction = MessageDirection.SENT,
                        )
                    _state.update { it.copy(messages = it.messages + msg) }
                } catch (e: RemoteException) {
                    _state.update { it.copy(error = "ping failed: ${e.message}", isConnected = false) }
                }
            }
        }

        private fun getMessages() {
            viewModelScope.launch {
                val svc =
                    service ?: run {
                        _sideEffect.send(AidlSideEffect.ShowMessage("Not connected — bind first"))
                        return@launch
                    }
                try {
                    val messages = withContext(Dispatchers.IO) { svc.messages }
                    _state.update { it.copy(remoteMessages = messages ?: emptyList()) }
                } catch (e: RemoteException) {
                    _state.update { it.copy(error = "getMessages failed: ${e.message}") }
                }
            }
        }

        private fun postMessage() {
            val content = _state.value.inputText.trim()
            if (content.isEmpty()) return
            viewModelScope.launch {
                val svc =
                    service ?: run {
                        _sideEffect.send(AidlSideEffect.ShowMessage("Not connected — bind first"))
                        return@launch
                    }
                try {
                    // postMessage is 'oneway' in AIDL — returns immediately without blocking
                    withContext(Dispatchers.IO) {
                        svc.postMessage(content, context.packageName)
                    }
                    val sent =
                        IpcMessage(
                            content = content,
                            sender = context.packageName,
                            method = IpcMethod.AIDL,
                            direction = MessageDirection.SENT,
                        )
                    _state.update { it.copy(messages = it.messages + sent, inputText = "") }
                } catch (e: RemoteException) {
                    _state.update { it.copy(error = "postMessage failed: ${e.message}") }
                }
            }
        }

        override fun onCleared() {
            super.onCleared()
            if (_state.value.isConnected) {
                runCatching { context.unbindService(serviceConnection) }
            }
        }
    }
