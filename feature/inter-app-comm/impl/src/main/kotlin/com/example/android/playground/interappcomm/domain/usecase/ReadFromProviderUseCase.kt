package com.example.android.playground.interappcomm.domain.usecase

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
 * Reads all messages from the OTHER app's [InterAppContentProvider].
 *
 * Cross-process security: the ContentProvider's `android:readPermission` gate
 * is enforced by the OS before our query even reaches the provider's [query] method.
 */
class ReadFromProviderUseCase @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    operator fun invoke(): Result<List<IpcMessage>> = runCatching {
        val targetPackage = InterAppCommConstants.getTargetPackage(context.packageName)
        val uri = "content://$targetPackage.provider.interapp/${InterAppCommConstants.PATH_MESSAGES}".toUri()

        val messages = mutableListOf<IpcMessage>()
        context.contentResolver.query(
            uri,
            null, // all columns
            null, // no selection
            null, // no selection args
            null, // default sort order
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(InterAppCommConstants.COLUMN_ID)
            val contentCol = cursor.getColumnIndexOrThrow(InterAppCommConstants.COLUMN_CONTENT)
            val senderCol = cursor.getColumnIndexOrThrow(InterAppCommConstants.COLUMN_SENDER)
            val timestampCol = cursor.getColumnIndexOrThrow(InterAppCommConstants.COLUMN_TIMESTAMP)
            while (cursor.moveToNext()) {
                messages.add(
                    IpcMessage(
                        id = cursor.getString(idCol),
                        content = cursor.getString(contentCol),
                        sender = cursor.getString(senderCol),
                        timestamp = cursor.getLong(timestampCol),
                        method = IpcMethod.CONTENT_PROVIDER,
                        direction = MessageDirection.RECEIVED,
                    ),
                )
            }
        }
        messages
    }
}
