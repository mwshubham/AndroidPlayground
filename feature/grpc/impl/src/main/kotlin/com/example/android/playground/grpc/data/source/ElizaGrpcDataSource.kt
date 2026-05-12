package com.example.android.playground.grpc.data.source

import com.example.android.playground.grpc.proto.ElizaServiceGrpcKt
import com.example.android.playground.grpc.proto.SayRequest
import io.grpc.ManagedChannel
import timber.log.Timber
import javax.inject.Inject

class ElizaGrpcDataSource
    @Inject
    constructor(
        private val channel: ManagedChannel,
    ) {
        private val stub by lazy { ElizaServiceGrpcKt.ElizaServiceCoroutineStub(channel) }

        suspend fun say(sentence: String): String =
            runCatching {
                val request =
                    SayRequest
                        .newBuilder()
                        .setSentence(sentence)
                        .build()
                stub.say(request).sentence
            }.getOrElse { e ->
                Timber.e(e, "gRPC say() failed")
                "Sorry, I could not process that."
            }
    }
