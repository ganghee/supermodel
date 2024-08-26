package com.github.ganghee.supermodel.create

fun createClassMessage(
    className: String,
    fields: List<String>,
    parameters: List<String>
): String {
    return """
<html>
<pre>
class $className {
  ${fields.joinToString("\n  ")}

  $className({
    ${parameters.joinToString("\n    ")}
  });
}
</pre>
</html>
    """.trimIndent().replace("\n", "<br>")
        .replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;")
        .replace(" ", "&nbsp;")
}
