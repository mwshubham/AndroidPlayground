package com.example.android.systemdesign.domain.repository

import com.example.android.systemdesign.domain.model.SystemDesignTopic

interface SystemDesignRepository {
    suspend fun getSystemDesignTopics(): List<SystemDesignTopic>
}
