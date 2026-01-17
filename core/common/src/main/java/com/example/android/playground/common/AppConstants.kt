package com.example.android.playground.common

/**
 * Common application constants used across all modules
 */
object AppConstants {
    /**
     * Default delay in milliseconds for simulating network operations
     */
    const val DEFAULT_DELAY = 3_000L

    /**
     * Debounce timeout for search query in milliseconds
     */
    const val SEARCH_DEBOUNCE_TIMEOUT = 300L

    /**
     * StateFlow subscription timeout in milliseconds for lifecycle awareness
     */
    const val STATEFLOW_SUBSCRIPTION_TIMEOUT = 5000L

    val loremIpsum =
        """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt 
        ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation 
        ullamco laboris nisi ut aliquip ex ea commodo consequat.
        """.trimIndent()
}
