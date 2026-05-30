package com.example.android.playground.roomdatabase.domain.usecase

import com.example.android.playground.roomdatabase.domain.repository.LibraryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SeedLibraryDataUseCaseTest {
    private val repository: LibraryRepository = mockk()
    private lateinit var useCase: SeedLibraryDataUseCase

    @Before
    fun setUp() {
        useCase = SeedLibraryDataUseCase(repository)
    }

    @Test
    fun `invoke calls seedInitialData on repository exactly once`() =
        runTest {
            coEvery { repository.seedInitialData() } just runs

            useCase()

            coVerify(exactly = 1) { repository.seedInitialData() }
        }
}
