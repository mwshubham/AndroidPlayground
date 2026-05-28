package com.example.android.playground.annotationprocessing.processor

/**
 * Declares a non-blank String validation rule for a property.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class NotBlank
