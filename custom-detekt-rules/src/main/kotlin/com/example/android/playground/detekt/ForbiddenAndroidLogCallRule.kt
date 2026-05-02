package com.example.android.playground.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression

/**
 * Detekt rule that forbids direct use of [android.util.Log] — both imported and
 * fully-qualified — without requiring type resolution.
 *
 * For the fully-qualified form `android.util.Log.d("tag", msg)`, the Kotlin PSI tree is:
 * ```
 * KtDotQualifiedExpression  (full text: android.util.Log.d(...))
 *   receiverExpression: android.util.Log
 *   selectorExpression: d(...)   <-- KtCallExpression, calleeExpression.text = "d"
 * ```
 *
 * This rule visits [KtDotQualifiedExpression] and checks whether its receiver text starts
 * with `android.util.Log` while its selector is a call expression — covering
 * fully-qualified calls like `android.util.Log.d(...)` that
 * [io.gitlab.arturbosch.detekt.rules.style.ForbiddenImport] would miss (no import line).
 *
 * [io.gitlab.arturbosch.detekt.rules.style.ForbiddenImport] (already active) handles
 * the `import android.util.Log` → `Log.d(...)` pattern.
 *
 * The legitimate exception — [com.example.android.playground.logging.ReleaseLogTree],
 * which IS the Timber backend — suppresses this rule with `@file:Suppress("ForbiddenAndroidLogCall")`.
 */
class ForbiddenAndroidLogCallRule(
    config: Config = Config.empty,
) : Rule(config) {
    override val issue =
        Issue(
            id = "ForbiddenAndroidLogCall",
            severity = Severity.CodeSmell,
            description = "Do not use android.util.Log directly. Use Timber instead.",
            debt = Debt.FIVE_MINS,
        )

    // Always active — no config needed to enable this rule.
    override val active: Boolean = true

    override fun visitDotQualifiedExpression(expression: KtDotQualifiedExpression) {
        super.visitDotQualifiedExpression(expression)
        val receiver = expression.receiverExpression.text
        val selector = expression.selectorExpression
        if (receiver.startsWith("android.util.Log") && selector is KtCallExpression) {
            val method = (selector as KtCallExpression).calleeExpression?.text ?: return
            report(
                CodeSmell(
                    issue,
                    Entity.from(expression),
                    "Forbidden call to 'android.util.Log.$method(...)'. " +
                        "Add implementation(libs.timber) to the module and use Timber.$method() instead.",
                ),
            )
        }
    }
}
