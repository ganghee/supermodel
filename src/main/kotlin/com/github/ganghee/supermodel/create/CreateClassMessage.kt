package com.github.ganghee.supermodel.create

fun createClassMessage(
    className: String,
    fields: List<String>,
    parameters: List<String>
): String {
    return """
<pre>
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
