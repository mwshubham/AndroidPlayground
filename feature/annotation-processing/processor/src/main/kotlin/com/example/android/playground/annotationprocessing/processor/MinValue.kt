package com.example.android.playground.annotationprocessing.processor

/**
 * Declares a minimum numeric value validation rule for a property.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class MinValue(
    val min: Int,
)
