package com.example.android.playground.annotationprocessing.processor

/**
 * Marks a class for compile-time `validate()` extension generation.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Validate
