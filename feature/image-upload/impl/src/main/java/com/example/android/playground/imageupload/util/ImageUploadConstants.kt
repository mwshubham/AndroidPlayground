package com.example.android.playground.imageupload.util

/**
 * Constants for the Image Upload module
 */
object ImageUploadConstants {
    /**
     * Screen name for analytics tracking
     */
    const val SCREEN_NAME = "ImageUpload"

    // Upload Configuration
    object Upload {
        /** Default number of images to upload in bulk operation */
        const val DEFAULT_UPLOAD_COUNT = 30

        /** Success rate for simulated uploads (80%) */
        const val SUCCESS_RATE = 0.8f

        /** Minimum network delay for upload simulation in milliseconds */
        const val MIN_UPLOAD_DELAY_MS = 100L

        /** Maximum network delay for upload simulation in milliseconds */
        const val MAX_UPLOAD_DELAY_MS = 300L

        /** Base URL for uploaded images */
        const val IMAGE_BASE_URL = "https://example.com/images/"

        /** File extension for uploaded images */
        const val IMAGE_EXTENSION = ".jpg"

        /** Prefix for generated image IDs */
        const val IMAGE_ID_PREFIX = "image"
    }

    // Format Strings
    object Formats {
        /** Image URL format */
        const val IMAGE_URL_FORMAT = "${Upload.IMAGE_BASE_URL}%s${Upload.IMAGE_EXTENSION}"

        /** Image ID format with timestamp and index */
        const val IMAGE_ID_FORMAT = "${Upload.IMAGE_ID_PREFIX}_%d_%d"
    }
}
