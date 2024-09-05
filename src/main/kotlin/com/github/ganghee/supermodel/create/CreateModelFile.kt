package com.github.ganghee.supermodel.create

import com.github.ganghee.supermodel.extensions.save
import com.github.ganghee.supermodel.extensions.toSnakeCase
import com.github.ganghee.supermodel.model.ModelInfo
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory

fun createModelFile(
    directory: PsiDirectory,
    rootClassName: String,
    modelItems: List<ModelInfo>,
    isSeparatedFile: Boolean
) {
    if (isSeparatedFile) {
        modelItems.forEach {
            createDataClassContent(
                isSeparatedFile = true,
                modelItems = listOf(it)
            ).save(
                srcDir = directory, fileName = "${it.className.toSnakeCase()}.dart"
            )
        }
    } else {
        createDataClassContent(
            isSeparatedFile = false,
            modelItems = modelItems
        ).save(
            srcDir = directory, fileName = "${rootClassName.toSnakeCase()}.dart"
        )
    }

    // Show the entered text in a message dialog
    Messages.showMessageDialog(
        "Your ClassName: $rootClassName", "Information", Messages.getInformationIcon()
    )
}