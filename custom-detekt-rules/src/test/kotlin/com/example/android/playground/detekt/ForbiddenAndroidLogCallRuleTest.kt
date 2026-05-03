package com.example.android.playground.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.test.lint
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ForbiddenAndroidLogCallRuleTest {
    private val rule = ForbiddenAndroidLogCallRule(Config.empty)

    // ─── Issue metadata ───────────────────────────────────────────────────────

    @Test
    fun `issue id is ForbiddenAndroidLogCall`() {
        assertEquals("ForbiddenAndroidLogCall", rule.issue.id)
    }

    @Test
    fun `issue severity is CodeSmell`() {
        assertEquals(Severity.CodeSmell, rule.issue.severity)
    }

    @Test
    fun `rule is always active`() {
        assertTrue(rule.active)
    }

    // ─── Detects fully-qualified Log calls ───────────────────────────────────

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
    fun `detects Log e call`() {
        val code = """fun foo() { android.util.Log.e("TAG", "error") }"""
        val findings = rule.lint(code)
        assertEquals(1, findings.size)
    }

    @Test
    fun `detects Log w call`() {
        val code = """fun foo() { android.util.Log.w("TAG", "warn") }"""
        val findings = rule.lint(code)
        assertEquals(1, findings.size)
    }

    @Test
    fun `detects Log i call`() {
        val code = """fun foo() { android.util.Log.i("TAG", "info") }"""
        val findings = rule.lint(code)
        assertEquals(1, findings.size)
    }

    @Test
    fun `detects Log v call`() {
        val code = """fun foo() { android.util.Log.v("TAG", "verbose") }"""
        val findings = rule.lint(code)
        assertEquals(1, findings.size)
    }

    @Test
    fun `detects Log wtf call`() {
        val code = """fun foo() { android.util.Log.wtf("TAG", "wtf") }"""
        val findings = rule.lint(code)
        assertEquals(1, findings.size)
    }

    // ─── Finding message content ──────────────────────────────────────────────

    @Test
    fun `finding message includes the method name and Timber guidance`() {
        val code = """fun foo() { android.util.Log.e("TAG", "error") }"""
        val findings = rule.lint(code)
        assertEquals(1, findings.size)
        val message = findings[0].message
        assertTrue(message.contains("Log.e(...)"), "Expected message to contain 'Log.e(...)' but was: $message")
        assertTrue(message.contains("Timber.e()"), "Expected message to contain 'Timber.e()' but was: $message")
    }

    // ─── Multiple findings ───────────────────────────────────────────────────

    @Test
    fun `detects multiple Log calls in the same function`() {
        val code =
            """
            fun foo() {
                android.util.Log.d("TAG", "debug")
                android.util.Log.e("TAG", "error")
                android.util.Log.w("TAG", "warn")
            }
            """.trimIndent()
        val findings = rule.lint(code)
        assertEquals(3, findings.size, "Expected 3 findings but got ${findings.size}: $findings")
    }

    // ─── Non-flagged patterns ────────────────────────────────────────────────

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

    @Test
    fun `does not flag unrelated dot qualified expressions`() {
        val code =
            """
            fun foo() {
                System.out.println("hello")
                java.lang.System.err.println("error")
            }
            """.trimIndent()
        val findings = rule.lint(code)
        assertEquals(0, findings.size, "Expected no findings but got: $findings")
    }

    @Test
    fun `does not flag android util Log property access`() {
        // android.util.Log.DEBUG is a property, not a call — selector is not KtCallExpression
        val code = """val level = android.util.Log.DEBUG"""
        val findings = rule.lint(code)
        assertEquals(0, findings.size, "Expected no findings but got: $findings")
    }
}
