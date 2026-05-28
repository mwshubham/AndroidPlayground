package com.example.android.playground.annotationprocessing.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class ValidateProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        ValidateProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
        )
}
