package com.example.android.playground.core.common.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Common utility for date formatting across the application
 */
object DateFormatter {

    /**
     * Standard date format with AM/PM for displaying timestamps
     * Format: "Jan 12, 2026 02:30 PM"
     */
    private const val STANDARD_DATE_FORMAT = "MMM dd, yyyy hh:mm a"

    /**
     * Formats a timestamp to a human-readable date string with AM/PM
     *
     * @param timestamp The timestamp in milliseconds
     * @return Formatted date string (e.g., "Jan 12, 2026 02:30 PM")
     */
    fun formatTimestamp(timestamp: Long): String {
        return formatTimestamp(
            timestamp = timestamp,
            pattern = STANDARD_DATE_FORMAT
        )

    }

    /**
     * Formats a timestamp using a custom format
     *
     * @param timestamp The timestamp in milliseconds
     * @param pattern The date format pattern
     * @return Formatted date string
     */
    fun formatTimestamp(timestamp: Long, pattern: String): String {
        val date = Date(timestamp)
        val dateFormatter = SimpleDateFormat(pattern, Locale.getDefault())
        return dateFormatter.format(date)
    }
}
