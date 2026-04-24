package com.example.android.playground.roomdatabase.presentation.state

import com.example.android.playground.roomdatabase.presentation.model.AuthorWithBooksUiModel
import com.example.android.playground.roomdatabase.presentation.model.BookWithTagsUiModel
import com.example.android.playground.roomdatabase.presentation.model.RoomDatabaseTab

data class RoomDatabaseState(
    val selectedTab: RoomDatabaseTab = RoomDatabaseTab.AUTHORS,
    val authorsWithBooks: List<AuthorWithBooksUiModel> = emptyList(),
    val booksWithTags: List<BookWithTagsUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
