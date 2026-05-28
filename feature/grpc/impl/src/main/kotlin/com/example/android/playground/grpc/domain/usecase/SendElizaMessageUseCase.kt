package com.example.android.playground.grpc.domain.usecase

import com.example.android.playground.grpc.domain.repository.GrpcRepository
import javax.inject.Inject

class SendElizaMessageUseCase
    @Inject
    constructor(
        private val repository: GrpcRepository,
    ) {
        suspend operator fun invoke(sentence: String): String = repository.sendMessage(sentence)
    }
