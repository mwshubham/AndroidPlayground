package com.example.android.playground.userinitiatedservice.domain.usecase

import com.example.android.playground.userinitiatedservice.domain.model.TransferItem
import com.example.android.playground.userinitiatedservice.domain.model.TransferStatus
import com.example.android.playground.userinitiatedservice.domain.repository.TransferRepository
import java.util.UUID
import javax.inject.Inject

private const val MIN_SIZE_MB = 5L
private const val SIZE_CYCLE_MB = 21L
private const val SIZE_STEP_MB = 4L
private const val BYTES_PER_MB = 1_000_000L

class AddTransferItemsUseCase
    @Inject
    constructor(
        private val repository: TransferRepository,
    ) {
        // Cycles through file-type pairs to produce realistic-looking names
        private val fileTypes =
            listOf(
                "photo" to "jpg",
                "video" to "mp4",
                "document" to "pdf",
                "audio" to "mp3",
                "screenshot" to "png",
            )

        suspend operator fun invoke(count: Int) {
            val now = System.currentTimeMillis()
            val items =
                (1..count).map { i ->
                    val (prefix, ext) = fileTypes[(i - 1) % fileTypes.size]
                    // Vary sizes between 5 MB and 25 MB so the UI is visually interesting
                    val sizeMb = MIN_SIZE_MB + ((i * SIZE_STEP_MB) % SIZE_CYCLE_MB)
                    TransferItem(
                        id = UUID.randomUUID().toString(),
                        name = "${prefix}_${"\u0025\u003003d".format(i)}.$ext",
                        sizeBytes = sizeMb * BYTES_PER_MB,
                        status = TransferStatus.PENDING,
                        totalChunks = 4,
                        uploadedChunks = 0,
                        createdAt = now + i,
                    )
                }
            repository.insertAll(items)
        }
    }
