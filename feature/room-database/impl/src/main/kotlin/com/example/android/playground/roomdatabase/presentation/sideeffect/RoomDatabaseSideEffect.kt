package com.example.android.playground.roomdatabase.presentation.sideeffect

sealed interface RoomDatabaseSideEffect {
    data object NavigateBack : RoomDatabaseSideEffect
}
