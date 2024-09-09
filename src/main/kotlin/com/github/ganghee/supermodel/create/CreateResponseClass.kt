package com.github.ganghee.supermodel.create

import com.github.ganghee.supermodel.extensions.addSuffix
import com.github.ganghee.supermodel.extensions.toSnakeCase
import com.github.ganghee.supermodel.model.ModelInfo

fun createResponseClass(
    isSeparatedFile: Boolean,
    modelItems: List<ModelInfo>
) = modelItems.joinToString("\n") {
    """
${if (isSeparatedFile) it.imports.joinToString("\n") else ""}

${
        if (it == modelItems[0] || isSeparatedFile) {
            "part '${it.className.toSnakeCase()}_response.g.dart';\n"
        } else {
            ""
        }
    }
@JsonSerializable(explicitToJson: true, createToJson: false)
class ${it.className}Response {
  ${
        it.fields.joinToString("\n  ") { fields ->
            val strings = fields.split(" ")
            "final " + strings[1].addSuffix("Response") + " " + strings[2].removeSuffix(";")
                .addSuffix("Response", type = strings[1]) + ';'
        }
    }
            
  ${it.className}Response({
    ${
        it.parameters.mapIndexed { index, parameter ->
            val strings = parameter.split(" ")
            "required " + strings[1].removeSuffix(",")
                .addSuffix("Response", type = it.fields[index].split(" ")[1]) + ','
        }.joinToString("\n    ")
    }
  });
}
""".trimIndent()
}
