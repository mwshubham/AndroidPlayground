package com.example.android.playground.detekt

import io.gitlab.arturbosch.detekt.api.Config
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ForbiddenAndroidLogRuleSetProviderTest {
    @Test
    fun `ruleSetId is custom-logging`() {
        val provider = ForbiddenAndroidLogRuleSetProvider()
        assertEquals("custom-logging", provider.ruleSetId)
    }

    @Test
    fun `instance returns RuleSet with id custom-logging`() {
        val provider = ForbiddenAndroidLogRuleSetProvider()
        val ruleSet = provider.instance(Config.empty)
        assertEquals("custom-logging", ruleSet.id)
    }

    @Test
    fun `instance returns RuleSet containing ForbiddenAndroidLogCallRule`() {
        val provider = ForbiddenAndroidLogRuleSetProvider()
        val ruleSet = provider.instance(Config.empty)
        assertTrue(
            ruleSet.rules.any { it is ForbiddenAndroidLogCallRule },
            "Expected RuleSet to contain ForbiddenAndroidLogCallRule but rules were: ${ruleSet.rules.map { it::class.simpleName }}",
        )
    }

    @Test
    fun `instance returns exactly one rule`() {
        val provider = ForbiddenAndroidLogRuleSetProvider()
        val ruleSet = provider.instance(Config.empty)
        assertEquals(1, ruleSet.rules.size, "Expected 1 rule but found: ${ruleSet.rules.size}")
    }
}
