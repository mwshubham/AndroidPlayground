package com.example.android.playground.sse.data.source

import com.example.android.playground.sse.data.dto.WikipediaChangeDto
import com.example.android.playground.sse.domain.model.SseEvent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

private const val WIKIPEDIA_SSE_URL = "https://stream.wikimedia.org/v2/stream/recentchange"

// Keep only human-readable article edits/creations from English Wikipedia.
// "categorize" and "log" events are extremely noisy and add no learning value.
private val ALLOWED_WIKIS = setOf("enwiki")
private val ALLOWED_TYPES = setOf("edit", "new")

class WikipediaEventSourceDataSource
    @Inject
    constructor(
        @Named("sse") private val okHttpClient: OkHttpClient,
        @Named("sse") private val json: Json,
    ) {
        fun observeChanges(): Flow<SseEvent> =
            callbackFlow {
                trySend(SseEvent.Connecting)

                val request =
                    Request
                        .Builder()
                        .url(WIKIPEDIA_SSE_URL)
                        .header("Accept", "text/event-stream")
                        .header("User-Agent", "AndroidPlayground/1.0 (educational; contact@example.com)")
                        .build()

                val eventSource =
                    EventSources
                        .createFactory(okHttpClient)
                        .newEventSource(
                            request = request,
                            listener =
                                object : EventSourceListener() {
                                    override fun onOpen(
                                        eventSource: EventSource,
                                        response: Response,
                                    ) {
                                        trySend(SseEvent.Connected)
                                    }

                                    override fun onEvent(
                                        eventSource: EventSource,
                                        id: String?,
                                        type: String?,
                                        data: String,
                                    ) {
                                        runCatching {
                                            val dto =
                                                json.decodeFromString<WikipediaChangeDto>(data)
                                            if (
                                                dto.title.isNotBlank() &&
                                                dto.wiki in ALLOWED_WIKIS &&
                                                dto.type in ALLOWED_TYPES
                                            ) {
                                                trySend(SseEvent.Change(dto.toDomain()))
                                            }
                                        }.onFailure { e ->
                                            Timber.w(e, "Failed to parse SSE event")
                                        }
                                    }

                                    override fun onClosed(eventSource: EventSource) {
                                        trySend(SseEvent.Disconnected)
                                        close()
                                    }

                                    override fun onFailure(
                                        eventSource: EventSource,
                                        t: Throwable?,
                                        response: Response?,
                                    ) {
                                        Timber.e(t, "SSE failure")
                                        trySend(SseEvent.Error(t?.message ?: "Connection failed"))
                                        close(t)
                                    }
                                },
                        )

                awaitClose { eventSource.cancel() }
            }
    }
