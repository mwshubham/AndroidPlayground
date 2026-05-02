package com.example.android.playground.roomdatabase.domain.usecase

import com.example.android.playground.roomdatabase.domain.repository.LibraryRepository
import javax.inject.Inject

class SeedLibraryDataUseCase
    @Inject
    constructor(
        private val repository: LibraryRepository,
    ) {
        suspend operator fun invoke() = repository.seedInitialData()
    }
