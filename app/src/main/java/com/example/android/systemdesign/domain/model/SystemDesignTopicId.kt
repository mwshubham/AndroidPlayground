package com.example.android.systemdesign.domain.model

sealed class SystemDesignTopicId() {
    data object ImageUploadApp : SystemDesignTopicId()
    data object NewsFeedApp : SystemDesignTopicId()
    data object ChatApp : SystemDesignTopicId()
    data object StockTradingApp : SystemDesignTopicId()
    data object PaginationLibrary : SystemDesignTopicId()
    data object HotelReservationApp : SystemDesignTopicId()
    data object GoogleDriveApp : SystemDesignTopicId()
    data object YouTubeApp : SystemDesignTopicId()
}
