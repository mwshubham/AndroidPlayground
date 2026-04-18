package com.example.android.playground.userinitiatedservice.domain.usecase

import com.example.android.playground.userinitiatedservice.domain.model.TransferItem
import com.example.android.playground.userinitiatedservice.domain.model.TransferStatus
import com.example.android.playground.userinitiatedservice.domain.repository.TransferRepository
import java.util.UUID
import javax.inject.Inject

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
                    val sizeMb = 5L + ((i * 4L) % 21L)
                    TransferItem(
                        id = UUID.randomUUID().toString(),
                        name = "${prefix}_${"%03d".format(i)}.$ext",
                        sizeBytes = sizeMb * 1_000_000L,
                        status = TransferStatus.PENDING,
                        totalChunks = 4,
                        uploadedChunks = 0,
                        createdAt = now + i,
                    )
                }
            repository.insertAll(items)
        }
    }
