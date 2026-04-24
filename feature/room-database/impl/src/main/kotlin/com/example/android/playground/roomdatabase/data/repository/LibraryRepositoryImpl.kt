package com.example.android.playground.roomdatabase.data.repository

import com.example.android.playground.roomdatabase.data.local.AuthorEntity
import com.example.android.playground.roomdatabase.data.local.BookEntity
import com.example.android.playground.roomdatabase.data.local.BookTagCrossRef
import com.example.android.playground.roomdatabase.data.local.ContactInfo
import com.example.android.playground.roomdatabase.data.local.LibraryDao
import com.example.android.playground.roomdatabase.data.local.TagEntity
import com.example.android.playground.roomdatabase.data.mapper.toDomain
import com.example.android.playground.roomdatabase.domain.model.AuthorWithBooks
import com.example.android.playground.roomdatabase.domain.model.BookWithTags
import com.example.android.playground.roomdatabase.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LibraryRepositoryImpl
    @Inject
    constructor(private val dao: LibraryDao) : LibraryRepository {

        override fun getAuthorsWithBooks(): Flow<List<AuthorWithBooks>> =
            dao.getAllAuthorsWithBooks().map { list -> list.map { it.toDomain() } }

        override fun getBooksWithTags(): Flow<List<BookWithTags>> =
            dao.getAllBooksWithTags().map { list -> list.map { it.toDomain() } }

        override suspend fun seedInitialData() {
            if (dao.getAuthorCount() > 0) return

            // ── Authors ───────────────────────────────────────────────────────
            val martinId = dao.insertAuthor(
                AuthorEntity(
                    name = "Robert C. Martin",
                    contact = ContactInfo(
                        email = "uncle.bob@cleancode.com",
                        website = "cleancoder.com",
                    ),
                ),
            )
            val fowlerId = dao.insertAuthor(
                AuthorEntity(
                    name = "Martin Fowler",
                    contact = ContactInfo(
                        email = "fowler@martinfowler.com",
                        website = "martinfowler.com",
                    ),
                ),
            )
            val kernighanId = dao.insertAuthor(
                AuthorEntity(
                    name = "Brian Kernighan",
                    contact = ContactInfo(
                        email = "bwk@cs.princeton.edu",
                        website = "cs.princeton.edu/~bwk",
                    ),
                ),
            )

            // ── Books ─────────────────────────────────────────────────────────
            val cleanCodeId = dao.insertBook(
                BookEntity(
                    title = "Clean Code",
                    authorId = martinId,
                    publishYear = 2008,
                    genres = listOf("Software Engineering", "Best Practices"),
                ),
            )
            val cleanArchId = dao.insertBook(
                BookEntity(
                    title = "Clean Architecture",
                    authorId = martinId,
                    publishYear = 2017,
                    genres = listOf("Software Engineering", "Architecture"),
                ),
            )
            val refactoringId = dao.insertBook(
                BookEntity(
                    title = "Refactoring",
                    authorId = fowlerId,
                    publishYear = 1999,
                    genres = listOf("Software Engineering", "Best Practices"),
                ),
            )
            val poeaaId = dao.insertBook(
                BookEntity(
                    title = "Patterns of Enterprise Application Architecture",
                    authorId = fowlerId,
                    publishYear = 2002,
                    genres = listOf("Architecture", "Design Patterns"),
                ),
            )
            val cProgrammingId = dao.insertBook(
                BookEntity(
                    title = "The C Programming Language",
                    authorId = kernighanId,
                    publishYear = 1978,
                    genres = listOf("Programming Languages", "Systems"),
                ),
            )
            val unixId = dao.insertBook(
                BookEntity(
                    title = "The Unix Programming Environment",
                    authorId = kernighanId,
                    publishYear = 1984,
                    genres = listOf("Systems", "Operating Systems"),
                ),
            )

            // ── Tags ──────────────────────────────────────────────────────────
            val classicId = dao.insertTag(TagEntity(name = "Classic"))
            val mustReadId = dao.insertTag(TagEntity(name = "Must Read"))
            val advancedId = dao.insertTag(TagEntity(name = "Advanced"))
            val beginnerFriendlyId = dao.insertTag(TagEntity(name = "Beginner Friendly"))
            val patternsId = dao.insertTag(TagEntity(name = "Patterns"))

            // ── Book ↔ Tag cross refs (many-to-many) ─────────────────────────
            dao.insertBookTagCrossRef(BookTagCrossRef(cleanCodeId, mustReadId))
            dao.insertBookTagCrossRef(BookTagCrossRef(cleanCodeId, beginnerFriendlyId))
            dao.insertBookTagCrossRef(BookTagCrossRef(cleanArchId, mustReadId))
            dao.insertBookTagCrossRef(BookTagCrossRef(cleanArchId, advancedId))
            dao.insertBookTagCrossRef(BookTagCrossRef(cleanArchId, patternsId))
            dao.insertBookTagCrossRef(BookTagCrossRef(refactoringId, classicId))
            dao.insertBookTagCrossRef(BookTagCrossRef(refactoringId, mustReadId))
            dao.insertBookTagCrossRef(BookTagCrossRef(poeaaId, classicId))
            dao.insertBookTagCrossRef(BookTagCrossRef(poeaaId, advancedId))
            dao.insertBookTagCrossRef(BookTagCrossRef(poeaaId, patternsId))
            dao.insertBookTagCrossRef(BookTagCrossRef(cProgrammingId, classicId))
            dao.insertBookTagCrossRef(BookTagCrossRef(cProgrammingId, mustReadId))
            dao.insertBookTagCrossRef(BookTagCrossRef(unixId, classicId))
            dao.insertBookTagCrossRef(BookTagCrossRef(unixId, advancedId))
        }
    }
