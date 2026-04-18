package com.example.android.playground.userinitiatedservice.domain.usecase

import com.example.android.playground.userinitiatedservice.domain.model.TransferItem
import com.example.android.playground.userinitiatedservice.domain.repository.TransferRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransferItemsUseCase
    @Inject
    constructor(
        private val repository: TransferRepository,
    ) {
        operator fun invoke(): Flow<List<TransferItem>> = repository.observeAll()
    }
