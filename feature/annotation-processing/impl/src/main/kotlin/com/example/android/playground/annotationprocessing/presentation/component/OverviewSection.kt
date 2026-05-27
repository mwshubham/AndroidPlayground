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

@Composable
internal fun OverviewSection(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "What are Annotations?",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text =
                "Annotations are metadata markers you attach to code elements — classes, " +
                    "functions, properties, or parameters — using the @ symbol. They carry " +
                    "information that can be read by the compiler, tools, or runtimes to trigger " +
                    "special behaviour.",
            style = MaterialTheme.typography.bodyMedium,
        )

        Text(
            text = "Compile-time vs Runtime",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text =
                "Annotations with @Retention(SOURCE) exist only in source code and are " +
                    "discarded before compilation output — used purely to feed annotation processors.\n\n" +
                    "Annotations with @Retention(BINARY) survive in the .class file but are not " +
                    "visible to reflection at runtime.\n\n" +
                    "Annotations with @Retention(RUNTIME) survive into the .dex / JVM bytecode and " +
                    "can be read via Kotlin/Java reflection.",
            style = MaterialTheme.typography.bodyMedium,
        )

        Text(
            text = "Annotation Processing",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text =
                "Annotation processors run as a separate phase during compilation. They " +
                    "inspect annotated symbols and can generate additional Kotlin or Java source " +
                    "files that are then fed back into the compiler. This eliminates boilerplate " +
                    "that would otherwise be written by hand — Room generates DAO implementations, " +
                    "Hilt generates DI factories, and in this demo AutoToStringProcessor generates " +
                    "a generatedToString() extension function.",
            style = MaterialTheme.typography.bodyMedium,
        )

        CodeSnippetCard(
            label = "Example — declaring an annotation",
            code =
                """
                @Target(AnnotationTarget.CLASS)
                @Retention(AnnotationRetention.SOURCE)
                annotation class AutoToString
                """.trimIndent(),
        )
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun OverviewSectionPreview() {
    PreviewContainer {
        OverviewSection()
    }
}
