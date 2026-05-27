package com.example.android.playground.annotationprocessing.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 * Entry point registered via the service-loader mechanism.
 *
 * KSP discovers this provider through the
 * `META-INF/services/com.google.devtools.ksp.processing.SymbolProcessorProvider` resource file.
 */
class AutoToStringProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        AutoToStringProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
        )
}
