import com.github.ganghee.supermodel.extensions.toSnakeCase
import com.github.ganghee.supermodel.model.Setting.isSeparatedFile

fun createFreezedPart(
    hasFreezedAnnotation: Boolean,
    isFreezed: Boolean,
    isFirst: Boolean,
    className: String
): String {
    return if ((isFirst && hasFreezedAnnotation) ||
        !isFirst && isSeparatedFile && isFreezed
    ) {
        "import 'package:freezed_annotation/freezed_annotation.dart';\n\npart '${className.toSnakeCase()}.freezed.dart';"
    } else {
        ""
    }
}

fun createGeneratorPart(
    hasJsonAnnotation: Boolean,
    hasJsonMethod: Boolean,
    isFirst: Boolean,
    isSeparatedFile: Boolean,
    className: String
): String {
    return if ((isFirst && hasJsonAnnotation) ||
        !isFirst && isSeparatedFile && hasJsonMethod
    ) {
        "part '${className.toSnakeCase()}.g.dart';\n"
    } else {
        ""
    }
}

fun createAnnotation(
    isFreezed: Boolean,
    isFromJsonSelected: Boolean,
    isToJsonSelected: Boolean,
): String {
    var annotation = if(isFreezed) "\n@freezed" else  "";
    annotation +=  when {
        isFromJsonSelected && isToJsonSelected ->
            "\n@JsonSerializable(explicitToJson: true)"

        isToJsonSelected ->
            "\n@JsonSerializable(explicitToJson: true, createFactory: false)"

        isFromJsonSelected ->
            "\n@JsonSerializable(explicitToJson: true, createToJson: false)"

        else -> ""
    }
    return annotation + "\n"
}