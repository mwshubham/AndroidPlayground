package com.example.android.playground.feed.data.repository

import com.example.android.playground.common.AppConstants
import com.example.android.playground.feed.domain.model.Topic
import com.example.android.playground.feed.domain.model.TopicId
import com.example.android.playground.feed.domain.repository.FeedRepository
import com.example.android.playground.feed.impl.R
import kotlinx.coroutines.delay
import javax.inject.Inject

class FeedRepositoryImpl
    @Inject
    constructor() : FeedRepository {
        override suspend fun getTopics(): List<Topic> {
            delay(AppConstants.DEFAULT_DELAY) // Simulate network delay
            // Mock data for the system design topics using sealed class and proper string resources
            return listOf(
                Topic(
                    id = TopicId.ImageUploadApp,
                    titleRes = R.string.topic_title_image_upload_app,
                    descriptionRes = R.string.topic_description_image_upload_app,
                ),
                Topic(
                    id = TopicId.LoginScreen,
                    titleRes = R.string.topic_title_login_screen,
                    descriptionRes = R.string.topic_description_login_screen,
                ),
                Topic(
                    id = TopicId.NoteApp,
                    titleRes = R.string.topic_title_note_app,
                    descriptionRes = R.string.topic_description_note_app,
                ),
                Topic(
                    id = TopicId.AndroidSecurity,
                    titleRes = R.string.topic_title_android_security,
                    descriptionRes = R.string.topic_description_android_security,
                ),
                Topic(
                    id = TopicId.MediaOrchestratorApp,
                    titleRes = R.string.topic_title_media_orchestrator,
                    descriptionRes = R.string.topic_description_media_orchestrator,
                ),
                Topic(
                    id = TopicId.UserInitiatedServiceApp,
                    titleRes = R.string.topic_title_user_initiated_service,
                    descriptionRes = R.string.topic_description_user_initiated_service,
                ),
                Topic(
                    id = TopicId.RoomDatabaseApp,
                    titleRes = R.string.topic_title_room_database,
                    descriptionRes = R.string.topic_description_room_database,
                ),
                Topic(
                    id = TopicId.InterAppCommunication,
                    titleRes = R.string.topic_title_inter_app_comm,
                    descriptionRes = R.string.topic_description_inter_app_comm,
                ),
            )
        }
    }
