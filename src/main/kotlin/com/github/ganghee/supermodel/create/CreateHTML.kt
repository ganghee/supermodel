import com.github.ganghee.supermodel.model.Option
import com.github.ganghee.supermodel.model.Setting.isSeparatedFile
import com.github.ganghee.supermodel.model.Setting.modelItems
import javax.swing.JLabel

fun createHTML(previewWidget: JLabel) {
    val htmlJsons = mutableListOf<String>()
    modelItems.forEachIndexed { index, it ->
        val htmlJsonText = createHTMLClass(
            index = index,
            className = it.className,
            fields = it.fields.distinct(),
            parameters = it.parameters.distinct(),
            imports = it.imports.distinct(),
            isSeparateCheckBoxSelected = isSeparatedFile,
            option = it.option,
            hasFreezedAnnotation = modelItems.any { it.option.isFreezedSelected },
            hasJsonAnnotation = modelItems.any { it.option.isFromJsonSelected || it.option.isToJsonSelected }
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
    hasJsonAnnotation: Boolean,
): String {
    var htmlClass: String =
        """
<pre>
${if (isSeparateCheckBoxSelected) imports.joinToString("\n") else ""}
${
            createFreezedPart(
                hasFreezedAnnotation = hasFreezedAnnotation,
                isFreezed = option.isFreezedSelected,
                isFirst = index == 0,
                className = className
            )
        }
${
            createGeneratorPart(
                hasJsonAnnotation = hasJsonAnnotation,
                hasJsonMethod = option.isFromJsonSelected || option.isToJsonSelected,
                isFirst = index == 0,
                isSeparatedFile = isSeparateCheckBoxSelected,
                className = className
            ).replace("\n", "<br>")
        }
""".trimIndent()

    htmlClass += createAnnotation(
        isFreezed = option.isFreezedSelected,
        isFromJsonSelected = option.isFromJsonSelected,
        isToJsonSelected = option.isToJsonSelected
    ).replace("\n", "<br>")
    htmlClass +=
        if (option.isFreezedSelected) {
            """
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
