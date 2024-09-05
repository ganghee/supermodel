package com.github.ganghee.supermodel.create

import com.github.ganghee.supermodel.model.ModelInfo

fun createDataClassContent(
    isSeparatedFile: Boolean,
    modelItems: List<ModelInfo>
) = modelItems.joinToString("\n\n") {
"""
${if(isSeparatedFile) it.imports.joinToString("\n") else ""}

class ${it.className} {
  ${it.fields.joinToString("\n  ")}
            
  ${it.className}({
    ${it.parameters.joinToString("\n    ")}
  });
}

""".trimIndent()
}
