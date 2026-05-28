package com.example.android.playground.core.common.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DateFormatterTest {
    // Use a fixed timezone so the tests are deterministic on any machine/CI.
    private val utc = TimeZone.getTimeZone("UTC")
    private val testLocale = Locale.US

    @Before
    fun setUp() {
        TimeZone.setDefault(utc)
    }

    // ------------------------------------------------------------------
    // formatTimestamp(Long) — default format "MMM dd, yyyy hh:mm a"
    // ------------------------------------------------------------------

    @Test
    fun `formatTimestamp returns non-empty string for positive timestamp`() {
        val result = DateFormatter.formatTimestamp(1_000_000_000_000L)
        assertNotNull(result)
        assert(result.isNotBlank())
    }

    @Test
    fun `formatTimestamp at epoch zero returns Jan 01 1970`() {
        val result = DateFormatter.formatTimestamp(0L)
        assert(result.contains("1970")) { "Expected 1970 in: $result" }
        assert(result.contains("Jan")) { "Expected Jan in: $result" }
    }

    @Test
    fun `formatTimestamp matches SimpleDateFormat output for same timestamp`() {
        val timestamp = 1_748_390_400_000L // 2025-05-27 UTC
        val expected = SimpleDateFormat("MMM dd, yyyy hh:mm a", testLocale).format(timestamp)
        assertEquals(expected, DateFormatter.formatTimestamp(timestamp))
    }

    // ------------------------------------------------------------------
    // formatTimestamp(Long, String) — custom pattern
    // ------------------------------------------------------------------

    @Test
    fun `formatTimestamp with custom pattern uses supplied pattern`() {
        val timestamp = 1_748_390_400_000L
        val pattern = "yyyy-MM-dd"
        val expected = SimpleDateFormat(pattern, testLocale).format(timestamp)
        assertEquals(expected, DateFormatter.formatTimestamp(timestamp = timestamp, pattern = pattern))
    }

    @Test
    fun `formatTimestamp with time-only pattern returns only time portion`() {
        val timestamp = 0L // 00:00:00 UTC
        val result = DateFormatter.formatTimestamp(timestamp = timestamp, pattern = "HH:mm:ss")
        assertEquals("00:00:00", result)
    }
}
