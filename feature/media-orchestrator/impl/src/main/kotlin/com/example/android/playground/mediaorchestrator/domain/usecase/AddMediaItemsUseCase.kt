package com.example.android.playground.mediaorchestrator.domain.usecase

import com.example.android.playground.mediaorchestrator.domain.model.MediaItem
import com.example.android.playground.mediaorchestrator.domain.model.UploadStatus
import com.example.android.playground.mediaorchestrator.domain.repository.MediaRepository
import java.util.UUID
import javax.inject.Inject

private const val CHUNKS_PER_ITEM = 5
private val SIMULATED_SIZES = listOf(1_024_000L, 2_048_000L, 3_072_000L, 4_096_000L, 512_000L)
private val SIMULATED_EXTENSIONS = listOf("jpg", "mp4", "png", "heic", "mov")

class AddMediaItemsUseCase
    @Inject
    constructor(private val repository: MediaRepository) {
        suspend operator fun invoke(count: Int) {
            val items =
                (1..count).map { index ->
                    MediaItem(
                        id = UUID.randomUUID().toString(),
                        name = "media_${index}.${SIMULATED_EXTENSIONS[(index - 1) % SIMULATED_EXTENSIONS.size]}",
                        sizeBytes = SIMULATED_SIZES[(index - 1) % SIMULATED_SIZES.size],
                        uploadStatus = UploadStatus.PENDING,
                        totalChunks = CHUNKS_PER_ITEM,
                        uploadedChunks = 0,
                        remoteUrl = null,
                        createdAt = System.currentTimeMillis(),
                    )
                }
            repository.insertAll(items)
        }
    }
