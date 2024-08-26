package com.github.ganghee.supermodel.model

import com.intellij.openapi.project.Project

fun createModel(
    packageName: String,
    className: String,
    project: Project,
    jsonText: String
) = """
class $className {
  final int? memberSeq;
  final String? memberNick;
  final String? imagePath;
  final String? memberType;
  final String? accessToken;
  final String jsonText = $jsonText;
  
  $className({
    required this.memberSeq,
    required this.memberNick,
    required this.imagePath,
    required this.memberType,
    required this.accessToken,
  });
}
""".trimIndent()