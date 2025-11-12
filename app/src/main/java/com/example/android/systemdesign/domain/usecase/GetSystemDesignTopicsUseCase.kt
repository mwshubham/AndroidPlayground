package com.example.android.systemdesign.domain.usecase

import com.example.android.systemdesign.domain.model.SystemDesignTopic
import com.example.android.systemdesign.domain.repository.SystemDesignRepository
import javax.inject.Inject

class GetSystemDesignTopicsUseCase @Inject constructor(
    private val repository: SystemDesignRepository
) {
    suspend operator fun invoke(): List<SystemDesignTopic> {
        return repository.getSystemDesignTopics()
    }
}
