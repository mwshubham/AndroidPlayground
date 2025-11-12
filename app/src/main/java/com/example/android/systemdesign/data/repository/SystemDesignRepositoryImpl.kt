package com.example.android.systemdesign.data.repository

import com.example.android.systemdesign.domain.model.SystemDesignTopic
import com.example.android.systemdesign.domain.repository.SystemDesignRepository
import javax.inject.Inject

class SystemDesignRepositoryImpl @Inject constructor() : SystemDesignRepository {

    override suspend fun getSystemDesignTopics(): List<SystemDesignTopic> {
        // Mock data for the system design topics
        var idCounter = 1
        return listOf(
            SystemDesignTopic(
                id = idCounter++,
                title = "Image Upload App",
                description = "Design a system for uploading images during a session"
            ),
            SystemDesignTopic(
                id = idCounter++,
                title = "News Feed App",
                description = "Design a news feed system that shows personalized content to users"
            ),
            SystemDesignTopic(
                id = idCounter++,
                title = "Chat App",
                description = "Design a real-time messaging application with multiple users"
            ),
            SystemDesignTopic(
                id = idCounter++,
                title = "Stock Trading App",
                description = "Design an application for real-time stock trading and monitoring"
            ),
            SystemDesignTopic(
                id = idCounter++,
                title = "Pagination Library",
                description = "Design a reusable pagination library for large datasets"
            ),
            SystemDesignTopic(
                id = idCounter++,
                title = "Hotel Reservation App",
                description = "Design a system for hotel booking and reservation management"
            ),
            SystemDesignTopic(
                id = idCounter++,
                title = "Google Drive App",
                description = "Design a cloud storage and file management system"
            ),
            SystemDesignTopic(
                id = idCounter++,
                title = "YouTube App",
                description = "Design a video streaming platform with upload and discovery features"
            )
        )
    }
}
