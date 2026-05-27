package com.example.android.playground.annotationprocessing.demo

import com.example.android.playground.annotationprocessing.processor.AutoJson

@AutoJson
data class SampleProduct(
    val name: String,
    val price: Double,
    val inStock: Boolean,
)
