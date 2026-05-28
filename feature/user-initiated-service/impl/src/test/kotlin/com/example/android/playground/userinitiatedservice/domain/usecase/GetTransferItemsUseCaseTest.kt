package com.example.android.playground.userinitiatedservice.domain.usecase

import com.example.android.playground.userinitiatedservice.domain.model.TransferItem
import com.example.android.playground.userinitiatedservice.domain.repository.TransferRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTransferItemsUseCaseTest {
    private val repository: TransferRepository = mockk()
    private lateinit var useCase: GetTransferItemsUseCase

    @Before
    fun setUp() {
        useCase = GetTransferItemsUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository observeAll`() =
        runTest {
            val items = emptyList<TransferItem>()
            every { repository.observeAll() } returns flowOf(items)

            val result = useCase().toList()

            assertEquals(listOf(items), result)
            verify(exactly = 1) { repository.observeAll() }
        }
}
