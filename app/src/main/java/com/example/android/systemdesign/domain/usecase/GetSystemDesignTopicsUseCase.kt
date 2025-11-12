package com.example.android.systemdesign.domain.usecase

import com.example.android.systemdesign.domain.model.SystemDesignTopic
import com.example.android.systemdesign.domain.repository.SystemDesignRepository

class GetSystemDesignTopicsUseCase(
    private val repository: SystemDesignRepository
) {
    suspend operator fun invoke(): List<SystemDesignTopic> {
        return repository.getSystemDesignTopics()
    }
}
