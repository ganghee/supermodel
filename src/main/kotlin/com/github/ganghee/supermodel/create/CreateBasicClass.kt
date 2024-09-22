package com.github.ganghee.supermodel.create

import com.github.ganghee.supermodel.model.ModelInfo
import createAnnotation
import createFreezedPart
import createGeneratorPart

fun createBasicClass(
    isSeparatedFile: Boolean,
    modelItems: List<ModelInfo>
) = modelItems.joinToString("\n") {
    """
${if (isSeparatedFile) it.imports.joinToString("\n") else ""}
${
        createFreezedPart(
            hasFreezedAnnotation = modelItems.any { it.option.isFreezedSelected },
            isFreezed = it.option.isFreezedSelected,
            isFirst = it == modelItems[0],
            className = it.className
        )
    }
${
        createGeneratorPart(
            hasJsonAnnotation = modelItems.any { it.option.isFromJsonSelected || it.option.isToJsonSelected },
            hasJsonMethod = it.option.isFromJsonSelected || it.option.isToJsonSelected,
            isFirst = it == modelItems[0],
            isSeparatedFile = isSeparatedFile,
            className = it.className
        )
    }
""".trimStart() + """
${
        createAnnotation(
            isFreezed = it.option.isFreezedSelected,
            isFromJsonSelected = it.option.isFromJsonSelected,
            isToJsonSelected = it.option.isToJsonSelected
        )
    }""" +
            if (it.option.isFreezedSelected) {
                """
class ${it.className} with _$${it.className} {
  factory ${it.className}({
    ${
                    it.fields.joinToString("\n    ") { parameter ->
                        val strings = parameter.split(" ")
                        "required " + strings[1] + " " + strings[2].replace(";", ",")
                    }
                }
  }) = _${it.className};
}
""".trimIndent()
            } else {
                """

class ${it.className} {
  ${it.fields.joinToString("\n  ")}
            
  ${it.className}({
    ${it.parameters.joinToString("\n    ")}
  });
}
""".trimIndent()
            }
}
