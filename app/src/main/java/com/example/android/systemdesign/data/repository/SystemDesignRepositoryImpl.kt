package com.example.android.systemdesign.data.repository

import com.example.android.systemdesign.R
import com.example.android.systemdesign.domain.model.SystemDesignTopic
import com.example.android.systemdesign.domain.model.SystemDesignTopicId
import com.example.android.systemdesign.domain.repository.SystemDesignRepository
import javax.inject.Inject

class SystemDesignRepositoryImpl @Inject constructor() : SystemDesignRepository {

    override suspend fun getSystemDesignTopics(): List<SystemDesignTopic> {
        // Mock data for the system design topics using sealed class and string resources
        return listOf(
            SystemDesignTopic(
                id = SystemDesignTopicId.ImageUploadApp,
                titleRes = R.string.topic_title_image_upload_app,
                descriptionRes = R.string.topic_description_image_upload_app
            ),
            SystemDesignTopic(
                id = SystemDesignTopicId.LoginScreen,
                titleRes = R.string.topic_title_login_screen,
                descriptionRes = R.string.topic_description_login_screen
            ),
            SystemDesignTopic(
                id = SystemDesignTopicId.NewsFeedApp,
                titleRes = R.string.topic_title_news_feed_app,
                descriptionRes = R.string.topic_description_news_feed_app
            ),
            SystemDesignTopic(
                id = SystemDesignTopicId.AnalyticsSDK,
                titleRes = R.string.topic_title_analytics_sdk,
                descriptionRes = R.string.topic_description_analytics_sdk
            ),
            SystemDesignTopic(
                id = SystemDesignTopicId.ChatApp,
                titleRes = R.string.topic_title_chat_app,
                descriptionRes = R.string.topic_description_chat_app
            ),
            SystemDesignTopic(
                id = SystemDesignTopicId.StockTradingApp,
                titleRes = R.string.topic_title_stock_trading_app,
                descriptionRes = R.string.topic_description_stock_trading_app
            ),
            SystemDesignTopic(
                id = SystemDesignTopicId.PaginationLibrary,
                titleRes = R.string.topic_title_pagination_library,
                descriptionRes = R.string.topic_description_pagination_library
            ),
            SystemDesignTopic(
                id = SystemDesignTopicId.HotelReservationApp,
                titleRes = R.string.topic_title_hotel_reservation_app,
                descriptionRes = R.string.topic_description_hotel_reservation_app
            ),
            SystemDesignTopic(
                id = SystemDesignTopicId.GoogleDriveApp,
                titleRes = R.string.topic_title_google_drive_app,
                descriptionRes = R.string.topic_description_google_drive_app
            ),
            SystemDesignTopic(
                id = SystemDesignTopicId.YouTubeApp,
                titleRes = R.string.topic_title_youtube_app,
                descriptionRes = R.string.topic_description_youtube_app
            )
        )
    }
}
