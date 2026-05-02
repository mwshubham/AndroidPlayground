package com.example.android.playground.roomdatabase.presentation.intent

import com.example.android.playground.roomdatabase.presentation.model.RoomDatabaseTab

sealed interface RoomDatabaseIntent {
    data object LoadData : RoomDatabaseIntent

    data class OnTabSelected(
        val tab: RoomDatabaseTab,
    ) : RoomDatabaseIntent

    data object NavigateBack : RoomDatabaseIntent
}
