package com.example.android.systemdesign.data.repository

import com.example.android.systemdesign.domain.model.SystemDesignTopic
import com.example.android.systemdesign.domain.repository.SystemDesignRepository
import javax.inject.Inject

class SystemDesignRepositoryImpl @Inject constructor() : SystemDesignRepository {

    override suspend fun getSystemDesignTopics(): List<SystemDesignTopic> {
        // Mock data for the system design topics
        return listOf(
            SystemDesignTopic(
                id = 1,
                title = "News Feed App",
                description = "Design a news feed system that shows personalized content to users"
            ),
            SystemDesignTopic(
                id = 2,
                title = "Chat App",
                description = "Design a real-time messaging application with multiple users"
            ),
            SystemDesignTopic(
                id = 3,
                title = "Stock Trading App",
                description = "Design an application for real-time stock trading and monitoring"
            ),
            SystemDesignTopic(
                id = 4,
                title = "Pagination Library",
                description = "Design a reusable pagination library for large datasets"
            ),
            SystemDesignTopic(
                id = 5,
                title = "Hotel Reservation App",
                description = "Design a system for hotel booking and reservation management"
            ),
            SystemDesignTopic(
                id = 6,
                title = "Google Drive App",
                description = "Design a cloud storage and file management system"
            ),
            SystemDesignTopic(
                id = 7,
                title = "YouTube App",
                description = "Design a video streaming platform with upload and discovery features"
            )
        )
    }
}
