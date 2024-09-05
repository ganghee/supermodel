import com.github.ganghee.supermodel.model.ModelInfo
import javax.swing.JLabel

fun createHTML(
    modelItems: List<ModelInfo>,
    isSeparateCheckBoxSelected: Boolean,
    previewWidget: JLabel
) {
    val htmlJsons = mutableListOf<String>()
    modelItems.forEach {
        val htmlJsonText = createHTMLClass(
            className = it.className,
            fields = it.fields.distinct(),
            parameters = it.parameters.distinct(),
            imports = it.imports.distinct(),
            isShowImports = isSeparateCheckBoxSelected,
        )
        htmlJsons.add(htmlJsonText)
    }
    previewWidget.text = "<html>" + htmlJsons.joinToString("") + "</html>"
}

fun createHTMLClass(
    className: String,
    fields: List<String>,
    parameters: List<String>,
    imports: List<String>,
    isShowImports: Boolean
): String {
    return """
<pre>
${if(isShowImports) imports.joinToString("\n") else ""}

class $className {
  ${
        fields.joinToString("\n  ")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
    }

  $className({
    ${parameters.joinToString("\n    ")}
  });
}
</pre>
    """.trimIndent().replace("\n", "<br>")
        .replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;")
        .replace(" ", "&nbsp;")
}
