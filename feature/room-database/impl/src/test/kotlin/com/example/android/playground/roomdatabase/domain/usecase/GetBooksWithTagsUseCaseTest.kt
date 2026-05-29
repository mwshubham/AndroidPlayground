package com.example.android.playground.roomdatabase.domain.usecase

import com.example.android.playground.roomdatabase.domain.model.BookWithTags
import com.example.android.playground.roomdatabase.domain.repository.LibraryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetBooksWithTagsUseCaseTest {
    private val repository: LibraryRepository = mockk()
    private lateinit var useCase: GetBooksWithTagsUseCase

    @Before
    fun setUp() {
        useCase = GetBooksWithTagsUseCase(repository)
    }

    @Test
    fun `invoke returns flow from repository`() =
        runTest {
            every { repository.getBooksWithTags() } returns flowOf(emptyList())

            val result = useCase().toList()

            assertEquals(listOf(emptyList<BookWithTags>()), result)
        }

    @Test
    fun `invoke delegates to repository exactly once`() =
        runTest {
            every { repository.getBooksWithTags() } returns flowOf(emptyList())

            useCase()

            verify(exactly = 1) { repository.getBooksWithTags() }
        }
}
