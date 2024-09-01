package com.github.ganghee.supermodel.create

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.ganghee.supermodel.extensions.toUpperCamelCase
import com.google.gson.Gson

fun createParameter(
    jsonText: String,
    dataModelItems: MutableList<Pair<List<String>, List<String>>>,
    onParameter: (fields: List<String>, parameters: List<String>) -> Unit
) {
    val mapper = jacksonObjectMapper()
    val map: Map<String, Any> = mapper.readValue(jsonText)
    val fields = mutableListOf<String>()
    val parameters = mutableListOf<String>()
    map.entries.forEach { (key, value) ->
        when (value) {
            is Int -> {
                fields.add("final int $key;")
                parameters.add("required this.$key,")
            }

            is String -> {
                println("jsonText12 ${key} ${value}")

                fields.add("final String $key;")
                parameters.add("required this.$key,")
            }

            is List<*> -> {
                when {
                    value.all { it is String } ->
                        fields.add("final List<String> $key;")

                    value.all { it is Int } ->
                        fields.add("final List<int> $key;")

                    value.all { it is Double } ->
                        fields.add("final List<double> $key;")

                    value.all { it is LinkedHashMap<*, *> } ->
                        fields.add("final List<double> $key;")

                    else ->
                        fields.add("final List<dynamic> $key;")

                }
                parameters.add("required this.$key,")
            }

            is Double -> {
                fields.add("final double $key;")
                parameters.add("required this.$key,")
            }

            is LinkedHashMap<*, *> -> {
                fields.add("final ${key.toUpperCamelCase()} $key;")
                parameters.add("required this.$key,")
                val gson = Gson()
                createParameter(
                    jsonText = gson.toJson(value).toString(),
                    dataModelItems = dataModelItems,
                    onParameter = { fields, parameters ->
                        dataModelItems.add(Pair(fields, parameters))
                    }
                )
            }
        }
    }
    onParameter(fields, parameters)
}