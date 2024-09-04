package com.github.ganghee.supermodel.create

import com.github.ganghee.supermodel.model.ModelInfo

fun createDataClassContent(
    modelItems: List<ModelInfo>
) = modelItems.joinToString("\n\n") {
"""
class ${it.className} {
  ${it.fields.joinToString("\n  ")}
            
  ${it.className}({
    ${it.parameters.joinToString("\n    ")}
  });
}
""".trimIndent()
}
