package com.example.android.playground.annotationprocessing.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer

private val kspPipelineStages =
    listOf(
        PipelineStage(
            step = 1,
            title = "Source code",
            description =
                "You write Kotlin source files. Classes annotated with @AutoToString are " +
                    "plain source — the annotation has @Retention(SOURCE) so it never appears in bytecode.",
        ),
        PipelineStage(
            step = 2,
            title = "KSP round begins",
            description =
                "Before the Kotlin compiler emits bytecode, KSP intercepts compilation " +
                    "and invokes every registered SymbolProcessorProvider found in the processor's " +
                    "META-INF/services resource file.",
        ),
        PipelineStage(
            step = 3,
            title = "Resolver.getSymbolsWithAnnotation()",
            description =
                "The processor asks the Resolver for all symbols annotated with " +
                    "@AutoToString. KSP returns a sequence of KSAnnotated elements — in our case " +
                    "KSClassDeclaration instances.",
        ),
        PipelineStage(
            step = 4,
            title = "Inspect class structure",
            description =
                "For each KSClassDeclaration, the processor walks getAllProperties() and " +
                    "filters for hasBackingField == true to collect the constructor-backed properties " +
                    "to include in the generated toString.",
        ),
        PipelineStage(
            step = 5,
            title = "CodeGenerator.createNewFile()",
            description =
                "A new .kt source file is written under build/generated/ksp/. The " +
                    "processor emits a top-level extension function fun ClassName.generatedToString().",
        ),
        PipelineStage(
            step = 6,
            title = "Compiler picks up generated sources",
            description =
                "KSP feeds the newly written files back into the Kotlin compiler. The " +
                    "generated extension is now part of the compilation unit, callable from any file " +
                    "that imports it.",
        ),
        PipelineStage(
            step = 7,
            title = "Final bytecode / .dex",
            description =
                "The compiler produces bytecode including both the original class and the " +
                    "generated extension. The @AutoToString annotation itself is stripped (SOURCE " +
                    "retention). The runtime never sees the annotation, only the generated function.",
        ),
    )

@Composable
internal fun PipelineSection(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "KSP Compile-Time Pipeline",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text =
                "This is how the @AutoToString annotation is processed from source code to " +
                    "the final generated extension function.",
            style = MaterialTheme.typography.bodyMedium,
        )
        kspPipelineStages.forEach { stage ->
            PipelineStageCard(stage = stage)
        }
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun PipelineSectionPreview() {
    PreviewContainer {
        PipelineSection()
    }
}
