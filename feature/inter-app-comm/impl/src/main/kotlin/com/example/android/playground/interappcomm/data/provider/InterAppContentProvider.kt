package com.example.android.playground.interappcomm.data.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.Binder
import com.example.android.playground.interappcomm.data.store.InterAppMessageStore
import com.example.android.playground.interappcomm.domain.model.IpcMessage
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.domain.model.MessageDirection
import com.example.android.playground.interappcomm.util.InterAppCommConstants
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import androidx.core.net.toUri

/**
 * Exposes an in-memory messages table to other apps via [android.content.ContentResolver].
 *
 * URI scheme: `content://{packageName}.provider.interapp/messages`
 *
 * Security layers:
 *  1. Manifest `android:readPermission` / `android:writePermission` — the OS enforces these
 *     before any [query] / [insert] / [delete] method is called. The caller must hold
 *     [CUSTOM_PERMISSION] or receive a SecurityException before reaching this code.
 *
 *  2. [Binder.getCallingPackage] — inside each method we log the caller's package for
 *     auditing. In a production provider you would cross-check against a whitelist here.
 *
 *  3. Projection validation in [query] — we only return our defined columns and ignore
 *     any column names the caller supplies to prevent information leakage.
 *
 *  4. No raw SQL passthrough — we use [MatrixCursor] and populate rows programmatically,
 *     eliminating any SQL-injection surface.
 *
 * Note on Hilt injection in ContentProvider:
 * ContentProvider.onCreate() is called before Application.onCreate(), which means
 * the Hilt component graph may not be ready yet. We therefore use the EntryPoint
 * pattern with lazy initialization inside each method body to ensure we only access
 * the Hilt graph after it has been initialised.
 */
class InterAppContentProvider : ContentProvider() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface InterAppContentProviderEntryPoint {
        fun interAppMessageStore(): InterAppMessageStore
    }

    private val store: InterAppMessageStore
        get() = EntryPointAccessors.fromApplication(
            context = context!!.applicationContext,
            entryPoint = InterAppContentProviderEntryPoint::class.java,
        ).interAppMessageStore()

    private val uriMatcher by lazy {
        UriMatcher(UriMatcher.NO_MATCH).apply {
            // matches: content://{authority}/messages
            addURI(requireNotNull(context).packageName + ".provider.interapp", InterAppCommConstants.PATH_MESSAGES, CODE_MESSAGES)
            // matches: content://{authority}/messages/{id}
            addURI(requireNotNull(context).packageName + ".provider.interapp", "${InterAppCommConstants.PATH_MESSAGES}/#", CODE_MESSAGE_ID)
        }
    }

    override fun onCreate(): Boolean = true // store is lazily resolved when first accessed

    override fun getType(uri: Uri): String =
        when (uriMatcher.match(uri)) {
            CODE_MESSAGES -> "vnd.android.cursor.dir/vnd.interappcomm.message"
            CODE_MESSAGE_ID -> "vnd.android.cursor.item/vnd.interappcomm.message"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

    /**
     * Returns rows from the in-memory store.
     *
     * Security: projection parameter is intentionally ignored — we return only
     * [ALL_COLUMNS] regardless, preventing the caller from requesting unknown columns.
     */
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor {
        logCaller("query")
        val cursor = MatrixCursor(ALL_COLUMNS)
        val messages = when (uriMatcher.match(uri)) {
            CODE_MESSAGES -> store.getContentProviderMessages()
            CODE_MESSAGE_ID -> {
                val id = uri.lastPathSegment
                store.getContentProviderMessages().filter { it.id == id }
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        messages.forEach { msg ->
            cursor.addRow(arrayOf<Any>(msg.id, msg.content, msg.sender, msg.timestamp))
        }
        return cursor
    }

    /**
     * Inserts a new message row.
     * Returns the URI of the newly inserted row: `content://{authority}/messages/{id}`.
     */
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        logCaller("insert")
        if (uriMatcher.match(uri) != CODE_MESSAGES) {
            throw IllegalArgumentException("insert only supported on /messages, got: $uri")
        }
        val content = values?.getAsString(InterAppCommConstants.COLUMN_CONTENT)
            ?: return null
        val sender = values.getAsString(InterAppCommConstants.COLUMN_SENDER) ?: "unknown"
        val timestamp = values.getAsLong(InterAppCommConstants.COLUMN_TIMESTAMP)
            ?: System.currentTimeMillis()

        val message = IpcMessage(
            content = content,
            sender = sender,
            timestamp = timestamp,
            method = IpcMethod.CONTENT_PROVIDER,
            direction = MessageDirection.RECEIVED,
        )
        val newId = store.insertContentProviderMessage(message)
        val authority = requireNotNull(context).packageName + ".provider.interapp"
        val newUri = ContentUris.withAppendedId(
            /* contentUri = */ "content://$authority/${InterAppCommConstants.PATH_MESSAGES}".toUri(),
            /* id = */ newId.hashCode().toLong(),
        )
            .buildUpon()
            .appendPath(newId)
            .build()
        context?.contentResolver?.notifyChange(uri, null)
        return newUri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        logCaller("delete")
        return when (uriMatcher.match(uri)) {
            CODE_MESSAGE_ID -> {
                val id = uri.lastPathSegment ?: return 0
                store.deleteContentProviderMessage(id)
                context?.contentResolver?.notifyChange(uri, null)
                1
            }

            CODE_MESSAGES -> {
                val countBefore = store.getContentProviderMessages().size
                store.clearContentProviderMessages()
                context?.contentResolver?.notifyChange(uri, null)
                countBefore
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int = 0 // update not demonstrated in this sample

    // ── Helpers ──────────────────────────────────────────────────────────────

    /**
     * Logs the calling app's package for auditing purposes.
     * In production, compare against a known whitelist after this check.
     */
    private fun logCaller(operation: String) {
        val callingPackage = callingPackage ?: "unknown"
        android.util.Log.d(
            "InterAppContentProvider",
            "$operation called by: $callingPackage (uid=${Binder.getCallingUid()})",
        )
    }



    companion object {
        private const val CODE_MESSAGES = 1
        private const val CODE_MESSAGE_ID = 2

        val ALL_COLUMNS = arrayOf(
            InterAppCommConstants.COLUMN_ID,
            InterAppCommConstants.COLUMN_CONTENT,
            InterAppCommConstants.COLUMN_SENDER,
            InterAppCommConstants.COLUMN_TIMESTAMP,
        )
    }
}
