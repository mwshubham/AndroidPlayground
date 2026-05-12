package com.example.android.playground.grpc.domain.repository

interface GrpcRepository {
    suspend fun sendMessage(sentence: String): String
}
