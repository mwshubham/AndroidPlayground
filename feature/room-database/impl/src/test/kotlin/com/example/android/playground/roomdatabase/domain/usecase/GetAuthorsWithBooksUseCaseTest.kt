package com.example.android.playground.roomdatabase.domain.usecase

import com.example.android.playground.roomdatabase.domain.model.Author
import com.example.android.playground.roomdatabase.domain.model.AuthorWithBooks
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

class GetAuthorsWithBooksUseCaseTest {
    private val repository: LibraryRepository = mockk()
    private lateinit var useCase: GetAuthorsWithBooksUseCase

    private val sampleAuthor =
        Author(
            id = 1L,
            name = "J.K. Rowling",
            email = "jk@books.com",
            website = "https://jkrowling.com",
        )
    private val sampleData = listOf(AuthorWithBooks(author = sampleAuthor, books = emptyList()))

    @Before
    fun setUp() {
        useCase = GetAuthorsWithBooksUseCase(repository)
    }

    @Test
    fun `invoke returns flow from repository`() =
        runTest {
            every { repository.getAuthorsWithBooks() } returns flowOf(sampleData)

            val result = useCase().toList()

            assertEquals(listOf(sampleData), result)
        }

    @Test
    fun `invoke returns empty list when no authors`() =
        runTest {
            every { repository.getAuthorsWithBooks() } returns flowOf(emptyList())

            val result = useCase().toList()

            assertEquals(listOf(emptyList<AuthorWithBooks>()), result)
        }

    @Test
    fun `invoke delegates to repository exactly once`() =
        runTest {
            every { repository.getAuthorsWithBooks() } returns flowOf(emptyList())

            useCase()

            verify(exactly = 1) { repository.getAuthorsWithBooks() }
        }
}
