package com.example.android.playground.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import java.io.File

class ForbiddenAndroidLogRuleSetProvider : RuleSetProvider {
    init {
        // Temporary diagnostic: verify ServiceLoader discovers this provider.
        File("/tmp/detekt-provider-loaded.txt").writeText("PROVIDER INSTANTIATED")
    }

    override val ruleSetId: String = "custom-logging"

    override fun instance(config: Config): RuleSet {
        val ruleSetConfig = config.subConfig(ruleSetId)
        return RuleSet(
            ruleSetId,
            listOf(ForbiddenAndroidLogCallRule(ruleSetConfig.subConfig("ForbiddenAndroidLogCall"))),
        )
    }
}
