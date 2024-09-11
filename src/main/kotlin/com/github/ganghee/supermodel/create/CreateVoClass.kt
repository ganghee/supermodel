package com.github.ganghee.supermodel.create

import com.github.ganghee.supermodel.extensions.addSuffix
import com.github.ganghee.supermodel.extensions.toSnakeCase
import com.github.ganghee.supermodel.model.ModelInfo

fun createVoClass(
    isSeparatedFile: Boolean,
    modelItems: List<ModelInfo>
) = modelItems.joinToString("\n") {
    """
${
        if (isSeparatedFile) it.imports.joinToString("\n") { import ->
            import.replace(
                ".dart",
                "_vo.dart"
            )
        } else ""
    }
${
        if (it == modelItems[0] || isSeparatedFile) {
            """
import 'package:domain/domain.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part '${it.className.toSnakeCase()}_vo.freezed.dart';

""".trimIndent()
        } else {
            ""
        }
    }
@freezed     
class ${it.className}Vo with _$${it.className}Vo {
  factory ${it.className}Vo({
    ${
        it.fields.joinToString("\n    ") { parameter ->
            val strings = parameter.split(" ")
            "required " + strings[1].addSuffix("Vo", isNotNull = true) + " " +
                    strings[2].removeSuffix(";")
                        .addSuffix("Vo", type = strings[1], isNotNull = true) + ','
        }
    }
  }) = _${it.className}Vo;
}

extension ${it.className}DtoExtension on ${it.className}Dto {
  ${it.className}Vo mapper() => ${it.className}Vo(
    ${
        it.fields.joinToString("\n    ") { fields ->
            val strings = fields.split(" ")
            strings[2].removeSuffix(";")
                .addSuffix("Vo", type = strings[1], isNotNull = true) + ": " + strings[2].removeSuffix(
                ";"
            )
                .addSuffix(
                    "Vo",
                    type = strings[1],
                    isNotNull = true
                ) + ","
        }
    }
  );
}
""".trimIndent()
}
