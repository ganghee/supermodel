package com.github.ganghee.supermodel.create

import com.github.ganghee.supermodel.extensions.addSuffix
import com.github.ganghee.supermodel.model.ModelInfo
import com.github.ganghee.supermodel.model.Setting.isSeparatedFile

fun createDtoClass(
    modelItems: List<ModelInfo>
) = modelItems.joinToString("\n") {
    """
${
        if (isSeparatedFile) it.imports.joinToString("\n") { import ->
            import.replace(
                ".dart",
                "_dto.dart"
            )
        } else ""
    }

class ${it.className}Dto {
  ${
        it.fields.joinToString("\n  ") { field ->
            val strings = field.split(" ")
            "final " + strings[1].addSuffix("Dto?") + " " + strings[2].removeSuffix(";").addSuffix(
                "Dto",
                type = strings[1],
                isNotNull = true
            ) + ';'
        }
    }
            
  ${it.className}Dto({
    ${
        it.parameters.mapIndexed { index, parameter ->
            val strings = parameter.split(" ")
            "required " + strings[1].removeSuffix(",").addSuffix(
                "Dto",
                type = it.fields[index].split(" ")[1],
                isNotNull = true
            ) + ','
        }.joinToString("\n    ")
    }
  });
}
""".trimIndent()
}
