package com.example.android.playground.annotationprocessing.processor

/**
 * Marks a class for compile-time `generatedToString()` extension generation.
 *
 * The [AutoToStringProcessor] KSP processor discovers every class annotated with
 * `@AutoToString` and writes a Kotlin source file containing a `fun ClassName.generatedToString(): String`
 * extension that formats all backing-field properties.
 *
 * Example:
 * ```kotlin
 * @AutoToString
 * data class SamplePerson(val name: String, val age: Int, val email: String)
 *
 * // Generated:
 * // fun SamplePerson.generatedToString(): String =
 * //     "SamplePerson(name=$name, age=$age, email=$email)"
 * ```
 *
 * The annotation is retained only in source; it is stripped before compilation output.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AutoToString
