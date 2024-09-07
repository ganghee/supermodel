package com.github.ganghee.supermodel.model

data class ModelInfo(
    val className: String,
    val fields: List<String>,
    val parameters: List<String>,
    val imports: List<String>,
    val option: Option = Option()
)

data class Option(
    val isFreezedSelected: Boolean = false,
    val isFromJsonSelected: Boolean = false,
    val isToJsonSelected: Boolean = false
)