package com.example.android.playground.feed.data.repository

import com.example.android.playground.feed.R
import com.example.android.playground.feed.domain.model.Topic
import com.example.android.playground.feed.domain.model.TopicId
import com.example.android.playground.feed.domain.repository.FeedRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor() : FeedRepository {

    override suspend fun getTopics(): List<Topic> {
        delay(3_000) // Simulate network delay
        // Mock data for the system design topics using sealed class and proper string resources
        return listOf(
            Topic(
                id = TopicId.ImageUploadApp,
                titleRes = R.string.topic_title_image_upload_app,
                descriptionRes = R.string.topic_description_image_upload_app
            ),
            Topic(
                id = TopicId.LoginScreen,
                titleRes = R.string.topic_title_login_screen,
                descriptionRes = R.string.topic_description_login_screen
            ),
            Topic(
                id = TopicId.NoteApp,
                titleRes = R.string.topic_title_note_app,
                descriptionRes = R.string.topic_description_note_app
            ),
            Topic(
                id = TopicId.NewsFeedApp,
                titleRes = R.string.topic_title_news_feed_app,
                descriptionRes = R.string.topic_description_news_feed_app
            ),
            Topic(
                id = TopicId.AnalyticsSDK,
                titleRes = R.string.topic_title_analytics_sdk,
                descriptionRes = R.string.topic_description_analytics_sdk
            ),
            Topic(
                id = TopicId.ChatApp,
                titleRes = R.string.topic_title_chat_app,
                descriptionRes = R.string.topic_description_chat_app
            ),
            Topic(
                id = TopicId.StockTradingApp,
                titleRes = R.string.topic_title_stock_trading_app,
                descriptionRes = R.string.topic_description_stock_trading_app
            ),
            Topic(
                id = TopicId.PaginationLibrary,
                titleRes = R.string.topic_title_pagination_library,
                descriptionRes = R.string.topic_description_pagination_library
            ),
            Topic(
                id = TopicId.HotelReservationApp,
                titleRes = R.string.topic_title_hotel_reservation_app,
                descriptionRes = R.string.topic_description_hotel_reservation_app
            ),
            Topic(
                id = TopicId.GoogleDriveApp,
                titleRes = R.string.topic_title_google_drive_app,
                descriptionRes = R.string.topic_description_google_drive_app
            ),
            Topic(
                id = TopicId.YouTubeApp,
                titleRes = R.string.topic_title_youtube_app,
                descriptionRes = R.string.topic_description_youtube_app
            )
        )
    }
}
