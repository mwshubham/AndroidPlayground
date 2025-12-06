package com.example.android.systemdesign.feed.domain.model

sealed class TopicId() {
    data object ImageUploadApp : TopicId()
    data object LoginScreen : TopicId()
    data object NoteApp : TopicId()
    data object NewsFeedApp : TopicId()
    data object AnalyticsSDK : TopicId()
    data object ChatApp : TopicId()
    data object StockTradingApp : TopicId()
    data object PaginationLibrary : TopicId()
    data object HotelReservationApp : TopicId()
    data object GoogleDriveApp : TopicId()
    data object YouTubeApp : TopicId()
}
