package com.example.android.playground.grpc.data.repository

import com.example.android.playground.grpc.data.source.ElizaGrpcDataSource
import com.example.android.playground.grpc.domain.repository.GrpcRepository
import javax.inject.Inject

class GrpcRepositoryImpl
    @Inject
    constructor(
        private val source: ElizaGrpcDataSource,
    ) : GrpcRepository {
        override suspend fun sendMessage(sentence: String): String = source.say(sentence)
    }
