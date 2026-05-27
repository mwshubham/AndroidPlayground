package com.example.android.playground.annotationprocessing.demo

import com.example.android.playground.annotationprocessing.processor.AutoToString

/**
 * Sample class annotated with [@AutoToString].
 *
 * At compile time the [com.example.android.playground.annotationprocessing.processor.AutoToStringProcessor]
 * KSP processor inspects this class and generates the file
 * `SamplePersonGeneratedToString.kt` containing:
 *
 * ```kotlin
 * fun SamplePerson.generatedToString(): String =
 *     "SamplePerson(name=$name, age=$age, email=$email)"
 * ```
 *
 * The generated file lands in `build/generated/ksp/debug/kotlin/` and is
 * automatically included in the compilation classpath — no manual wiring needed.
 */
@AutoToString
data class SamplePerson(
    val name: String,
    val age: Int,
    val email: String,
)
