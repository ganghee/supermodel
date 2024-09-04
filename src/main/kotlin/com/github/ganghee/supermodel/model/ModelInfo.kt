package com.github.ganghee.supermodel.model

data class ModelInfo(
    val className: String,
    val fields: List<String>,
    val parameters: List<String>,
    val imports: List<String>
)
