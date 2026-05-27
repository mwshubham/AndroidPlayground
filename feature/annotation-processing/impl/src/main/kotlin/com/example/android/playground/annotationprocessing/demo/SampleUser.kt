package com.example.android.playground.annotationprocessing.demo

import com.example.android.playground.annotationprocessing.processor.MinValue
import com.example.android.playground.annotationprocessing.processor.NotBlank
import com.example.android.playground.annotationprocessing.processor.Validate

private const val MINIMUM_USER_AGE = 18

@Validate
data class SampleUser(
    @NotBlank
    val username: String,
    @MinValue(MINIMUM_USER_AGE)
    val age: Int,
    @NotBlank
    val email: String,
)
