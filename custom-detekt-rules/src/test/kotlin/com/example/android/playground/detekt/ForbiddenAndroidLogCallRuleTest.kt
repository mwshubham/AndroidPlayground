package com.example.android.playground.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.lint
import kotlin.test.Test
import kotlin.test.assertEquals

class ForbiddenAndroidLogCallRuleTest {
    private val rule = ForbiddenAndroidLogCallRule(Config.empty)

    @Test
    fun `detects fully qualified android util Log call on single line`() {
        val code =
            """
            fun foo() {
                android.util.Log.d("TAG", "message")
            }
            """.trimIndent()
        val findings = rule.lint(code)
        assertEquals(1, findings.size, "Expected 1 finding but got ${findings.size}: $findings")
    }

    @Test
    fun `detects fully qualified android util Log call spanning multiple lines`() {
        val code =
            """
            fun foo() {
                android.util.Log.d(
                    "TAG",
                    "message",
                )
            }
            """.trimIndent()
        val findings = rule.lint(code)
        assertEquals(1, findings.size, "Expected 1 finding but got ${findings.size}: $findings")
    }

    @Test
    fun `does not flag Timber calls`() {
        val code =
            """
            fun foo() {
                Timber.d("message")
            }
            """.trimIndent()
        val findings = rule.lint(code)
        assertEquals(0, findings.size, "Expected no findings but got: $findings")
    }
}
