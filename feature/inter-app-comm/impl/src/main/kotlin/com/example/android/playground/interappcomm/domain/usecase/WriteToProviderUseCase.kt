package com.example.android.playground.interappcomm.domain.usecase

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import com.example.android.playground.interappcomm.domain.model.IpcMessage
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.domain.model.MessageDirection
import com.example.android.playground.interappcomm.util.InterAppCommConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.net.toUri

/**
 * Inserts a message into the OTHER app's [InterAppContentProvider].
 *
 * Cross-process security: the ContentProvider manifest declaration sets
 * `android:writePermission`, so the OS rejects this insert if the caller
 * (this app) does not hold [CUSTOM_PERMISSION]. Both flavors hold the permission
 * because they share the same signing certificate.
 */
class WriteToProviderUseCase @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    operator fun invoke(content: String): Result<IpcMessage> = runCatching {
        val targetPackage = InterAppCommConstants.getTargetPackage(context.packageName)
        val uri = "content://$targetPackage.provider.interapp/${InterAppCommConstants.PATH_MESSAGES}".toUri()

        val values = ContentValues().apply {
            put(InterAppCommConstants.COLUMN_CONTENT, content)
            put(InterAppCommConstants.COLUMN_SENDER, context.packageName)
            put(InterAppCommConstants.COLUMN_TIMESTAMP, System.currentTimeMillis())
        }
        val resultUri = context.contentResolver.insert(uri, values)
            ?: error("ContentProvider insert returned null — target app may not be installed")

        IpcMessage(
            id = resultUri.lastPathSegment ?: "",
            content = content,
            sender = context.packageName,
            method = IpcMethod.CONTENT_PROVIDER,
            direction = MessageDirection.SENT,
        )
    }
}
