package com.github.ganghee.supermodel.create

import com.github.ganghee.supermodel.extensions.addSuffix
import com.github.ganghee.supermodel.extensions.toSnakeCase
import com.github.ganghee.supermodel.model.ModelInfo

fun createResponseClass(
    isSeparatedFile: Boolean,
    modelItems: List<ModelInfo>
) = modelItems.joinToString("\n") {
    """
${if (isSeparatedFile) it.imports.joinToString("\n") { import ->
        import.replace(
            ".dart",
            "_response.dart"
        )
    }else ""}
${
        if (it == modelItems[0] || isSeparatedFile) {
            "import 'package:data/src/mapper/data_to_domain_mapper.dart';\n" +
                    "import 'package:domain/domain.dart';\n\n" +
                    "import 'package:json_annotation/json_annotation.dart';\n" +
                    "part '${it.className.toSnakeCase()}_response.g.dart';\n"
        } else {
            ""
        }
    }
@JsonSerializable(explicitToJson: true, createToJson: false)
class ${it.className}Response extends DataToDomainMapper<${it.className}Dto> {
  ${
        it.fields.joinToString("\n  ") { fields ->
            val strings = fields.split(" ")
            "final " + strings[1].addSuffix("Response?") + " " + strings[2].removeSuffix(";")
                .addSuffix("Response", type = strings[1], isNotNull = true) + ';'
        }
    }
            
  ${it.className}Response({
    ${
        it.parameters.mapIndexed { index, parameter ->
            val strings = parameter.split(" ")
            "required " + strings[1].removeSuffix(",")
                .addSuffix("Response", type = it.fields[index].split(" ")[1], isNotNull = true) + ','
        }.joinToString("\n    ")
    }
  });
  
  @override
  ${it.className}Dto mapper() => ${it.className}Dto(
    ${
        it.fields.joinToString("\n    ") { fields ->
            val strings = fields.split(" ")
            strings[2].removeSuffix(";").addSuffix(
                "Response",
                type = strings[1],
                isNotNull = true
            ) + ": " + strings[2].removeSuffix(";")
                .addSuffix("Response", type = strings[1], isNotNull = true) + ","
        }
    }
  );

  factory ${it.className}Response.fromJson(Map<String, dynamic> json) =>
      _$${it.className}ResponseFromJson(json);
}
""".trimIndent()
}
