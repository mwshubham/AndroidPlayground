package com.example.android.playground.websocket.data.source

import com.example.android.playground.websocket.data.dto.BinanceTickerDto
import com.example.android.playground.websocket.domain.model.WebSocketEvent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

private const val BINANCE_WS_URL = "wss://stream.binance.com:9443/ws/btcusdt@ticker"
private const val WS_CLOSE_NORMAL = 1000

class BinanceWebSocketSource
    @Inject
    constructor(
        @Named("websocket") private val okHttpClient: OkHttpClient,
        @Named("websocket") private val json: Json,
    ) {
        fun observeTicker(): Flow<WebSocketEvent> =
            callbackFlow {
                trySend(WebSocketEvent.Connecting)

                val request = Request.Builder().url(BINANCE_WS_URL).build()

                val webSocket =
                    okHttpClient.newWebSocket(
                        request = request,
                        listener =
                            object : WebSocketListener() {
                                override fun onOpen(
                                    webSocket: WebSocket,
                                    response: Response,
                                ) {
                                    trySend(WebSocketEvent.Connected)
                                }

                                override fun onMessage(
                                    webSocket: WebSocket,
                                    text: String,
                                ) {
                                    runCatching {
                                        val dto = json.decodeFromString<BinanceTickerDto>(text)
                                        trySend(WebSocketEvent.Tick(dto.toDomain()))
                                    }.onFailure { e ->
                                        Timber.w(e, "Failed to parse ticker message")
                                    }
                                }

                                override fun onClosed(
                                    webSocket: WebSocket,
                                    code: Int,
                                    reason: String,
                                ) {
                                    trySend(WebSocketEvent.Disconnected(reason))
                                    close()
                                }

                                override fun onFailure(
                                    webSocket: WebSocket,
                                    t: Throwable,
                                    response: Response?,
                                ) {
                                    Timber.e(t, "WebSocket failure")
                                    trySend(WebSocketEvent.Error(t.message ?: "Connection failed"))
                                    close(t)
                                }
                            },
                    )

                awaitClose {
                    webSocket.close(WS_CLOSE_NORMAL, "Client disconnecting")
                }
            }
    }
