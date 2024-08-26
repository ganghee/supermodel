package com.github.ganghee.supermodel.create

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

private fun createParameter(jsonText: String) {
    val mapper = jacksonObjectMapper()
    val map: Map<String, Any> = mapper.readValue(jsonText)
    map.entries.forEach { (key, value) ->
        println("map2 key $key = $value")
        println("map2 value Int ${value is Int}")
        println("map2 value String ${value is String}")
        println("map2 value List ${value is List<*>}")
        println("map2 value Double ${value is Double}")
        println("value1234 ${value is Map<*, *>}")
    }
}