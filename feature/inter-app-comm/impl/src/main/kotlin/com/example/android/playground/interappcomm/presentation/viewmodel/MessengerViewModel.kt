package com.example.android.playground.interappcomm.presentation.viewmodel

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.interappcomm.domain.model.IpcMessage
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.domain.model.MessageDirection
import com.example.android.playground.interappcomm.presentation.intent.MessengerIntent
import com.example.android.playground.interappcomm.presentation.sideeffect.MessengerSideEffect
import com.example.android.playground.interappcomm.presentation.state.MessengerState
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
class MessengerViewModel
    @Inject
    constructor(
        @param:ApplicationContext private val context: Context,
    ) : ViewModel() {
        private val _state = MutableStateFlow(MessengerState())
        val state: StateFlow<MessengerState> = _state.asStateFlow()

        private val _sideEffect = Channel<MessengerSideEffect>(Channel.BUFFERED)
        val sideEffect = _sideEffect.receiveAsFlow()

        private var serverMessenger: Messenger? = null

        /**
         * Handler for INCOMING messages (echoes) from the server service.
         * Runs on the main thread — no coroutine needed for UI updates from here.
         */
        private val incomingHandler =
            object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    when (msg.what) {
                        InterAppCommConstants.MSG_ECHO -> {
                            val content = msg.data.getString(InterAppCommConstants.KEY_MESSAGE_CONTENT) ?: return
                            val sender = msg.data.getString(InterAppCommConstants.KEY_SENDER_PACKAGE) ?: "unknown"
                            val received =
                                IpcMessage(
                                    content = content,
                                    sender = sender,
                                    method = IpcMethod.MESSENGER,
                                    direction = MessageDirection.RECEIVED,
                                )
                            _state.update { it.copy(messages = it.messages + received) }
                        }
                        else -> super.handleMessage(msg)
                    }
                }
            }
        private val replyMessenger = Messenger(incomingHandler)

        private val serviceConnection =
            object : ServiceConnection {
                override fun onServiceConnected(
                    name: ComponentName,
                    service: IBinder,
                ) {
                    serverMessenger = Messenger(service)
                    _state.update { it.copy(isConnected = true, isConnecting = false, error = null) }
                }

                override fun onServiceDisconnected(name: ComponentName) {
                    serverMessenger = null
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

        fun handleIntent(intent: MessengerIntent) {
            when (intent) {
                MessengerIntent.Connect -> connect()
                MessengerIntent.Disconnect -> disconnect()
                is MessengerIntent.OnInputChanged -> _state.update { it.copy(inputText = intent.text) }
                MessengerIntent.SendMessage -> sendMessage()
                MessengerIntent.NavigateBack -> {
                    if (_state.value.isConnected) disconnect()
                    viewModelScope.launch { _sideEffect.send(MessengerSideEffect.NavigateBack) }
                }
            }
        }

        private fun connect() {
            val targetPackage = _state.value.targetPackage
            val intent =
                Intent().apply {
                    setClassName(targetPackage, InterAppCommConstants.MESSENGER_SERVICE_CLASS)
                }
            _state.update { it.copy(isConnecting = true, error = null) }
            val bound = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            if (!bound) {
                _state.update {
                    it.copy(
                        isConnecting = false,
                        error = "Could not bind — is $targetPackage installed?",
                    )
                }
            }
        }

        private fun disconnect() {
            runCatching { context.unbindService(serviceConnection) }
            serverMessenger = null
            _state.update { it.copy(isConnected = false, isConnecting = false) }
        }

        private fun sendMessage() {
            val content = _state.value.inputText.trim()
            if (content.isEmpty()) return
            val server = serverMessenger
            if (server == null) {
                viewModelScope.launch {
                    _sideEffect.send(MessengerSideEffect.ShowMessage("Not connected — bind first"))
                }
                return
            }
            val msg = Message.obtain(null, InterAppCommConstants.MSG_SEND_TEXT)
            msg.data =
                Bundle().apply {
                    putString(InterAppCommConstants.KEY_MESSAGE_CONTENT, content)
                    putString(InterAppCommConstants.KEY_SENDER_PACKAGE, context.packageName)
                }
            msg.replyTo = replyMessenger
            try {
                server.send(msg)
                val sent =
                    IpcMessage(
                        content = content,
                        sender = context.packageName,
                        method = IpcMethod.MESSENGER,
                        direction = MessageDirection.SENT,
                    )
                _state.update { it.copy(messages = it.messages + sent, inputText = "") }
            } catch (e: RemoteException) {
                _state.update { it.copy(error = "Send failed: ${e.message}", isConnected = false) }
            }
        }

        override fun onCleared() {
            super.onCleared()
            if (_state.value.isConnected) {
                runCatching { context.unbindService(serviceConnection) }
            }
        }
    }
