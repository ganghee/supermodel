import com.github.ganghee.supermodel.extensions.toSnakeCase
import com.github.ganghee.supermodel.model.ModelInfo
import com.github.ganghee.supermodel.model.Option
import javax.swing.JLabel

fun createHTML(
    modelItems: List<ModelInfo>,
    isSeparateCheckBoxSelected: Boolean,
    previewWidget: JLabel,
) {
    val htmlJsons = mutableListOf<String>()
    modelItems.forEachIndexed { index, it ->
        val htmlJsonText = createHTMLClass(
            index = index,
            className = it.className,
            fields = it.fields.distinct(),
            parameters = it.parameters.distinct(),
            imports = it.imports.distinct(),
            isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
            option = it.option,
            hasFreezedAnnotation = modelItems.any { it.option.isFreezedSelected }
        )
        htmlJsons.add(htmlJsonText)
    }
    previewWidget.text = "<html>" + htmlJsons.joinToString("") + "</html>"
}

fun createHTMLClass(
    index: Int,
    className: String,
    fields: List<String>,
    parameters: List<String>,
    imports: List<String>,
    isSeparateCheckBoxSelected: Boolean,
    option: Option,
    hasFreezedAnnotation: Boolean,
): String {
    var htmlClass: String =
        """
<pre>
${if (isSeparateCheckBoxSelected) imports.joinToString("\n") else ""}
${if ((index == 0 && hasFreezedAnnotation && !isSeparateCheckBoxSelected) || (index != 0 && isSeparateCheckBoxSelected && option.isFreezedSelected)) "import 'package:freezed_annotation/freezed_annotation.dart';\n\npart '" + className.toSnakeCase() + ".freezed.dart';\n" else ""}
""".trimIndent()

    htmlClass +=
        if (option.isFreezedSelected) {
            """
                
@freezed
class $className with _$$className {
  factory $className({
    ${
                fields.joinToString("\n    ") {
                    val strings = it.split(" ")
                    "required " + strings[1] + " " + strings[2].replace(";", ",")
                }
            }
  }) = _$className;
}

</pre>
""".trimIndent().replace("\n", "<br>")
                .replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;")
                .replace(" ", "&nbsp;")
        } else {
            """

class $className {
  ${
                fields.joinToString("\n  ")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
            }

  $className({
    ${parameters.joinToString("\n    ")}
  });
  
</pre>
""".trimIndent().replace("\n", "<br>")
                .replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;")
                .replace(" ", "&nbsp;")

        }
    return htmlClass

}
