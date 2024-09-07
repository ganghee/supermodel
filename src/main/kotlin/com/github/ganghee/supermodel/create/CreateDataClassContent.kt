package com.github.ganghee.supermodel.create

import com.github.ganghee.supermodel.extensions.toSnakeCase
import com.github.ganghee.supermodel.model.ModelInfo

fun createDataClassContent(
    isSeparatedFile: Boolean,
    modelItems: List<ModelInfo>
) = modelItems.joinToString("\n") {
    """
${if (isSeparatedFile) it.imports.joinToString("\n") else ""}
${
        if (isCreateFreezedPart(
                modelItems = modelItems,
                isFreezed = it.option.isFreezedSelected,
                isFirst = it == modelItems[0],
                isSeparatedFile = isSeparatedFile
            )
        ) "import 'package:freezed_annotation/freezed_annotation.dart';\n\npart '${it.className.toSnakeCase()}.freezed.dart';" else ""
    }
    
""".trimStart() + if (it.option.isFreezedSelected) {
        """

@freezed
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

private fun isCreateFreezedPart(
    modelItems: List<ModelInfo>,
    isFreezed: Boolean,
    isFirst: Boolean,
    isSeparatedFile: Boolean
): Boolean = (isFirst && modelItems.any { it.option.isFreezedSelected }) ||
        !isFirst && isSeparatedFile && isFreezed
