package com.example.android.playground.feed.domain.model

sealed class TopicId {
    data object ImageUploadApp : TopicId()

    data object LoginScreen : TopicId()

    data object NoteApp : TopicId()

    data object MediaOrchestratorApp : TopicId()

    data object UserInitiatedServiceApp : TopicId()

    data object AndroidSecurity : TopicId()

    data object RoomDatabaseApp : TopicId()

    data object InterAppCommunication : TopicId()
}
