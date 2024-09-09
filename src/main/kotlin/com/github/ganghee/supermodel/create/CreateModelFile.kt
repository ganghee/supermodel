package com.github.ganghee.supermodel.create

import com.github.ganghee.supermodel.extensions.save
import com.github.ganghee.supermodel.extensions.toSnakeCase
import com.github.ganghee.supermodel.model.ModelInfo
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory

fun createModelFile(
    basicDirectory: PsiDirectory,
    isCheckedResponse: Boolean,
    isCheckedDto: Boolean,
    isCheckedVo: Boolean,
    responsePsiDirectory: PsiDirectory?,
    dtoDirectory: PsiDirectory?,
    voDirectory: PsiDirectory?,
    rootClassName: String,
    modelItems: List<ModelInfo>,
    isSeparatedFile: Boolean
) {


    if(isCheckedResponse && responsePsiDirectory != null) {
        if (isSeparatedFile) {
            modelItems.forEach {
                createResponseClass(
                    isSeparatedFile = true,
                    modelItems = listOf(it)
                ).save(
                    srcDir = responsePsiDirectory, fileName = "${it.className.toSnakeCase()}_response.dart"
                )
            }
        } else {
            createResponseClass(
                isSeparatedFile = false,
                modelItems = modelItems
            ).save(
                srcDir = responsePsiDirectory, fileName = "${rootClassName.toSnakeCase()}_response.dart"
            )
        }
    } else {
        if (isSeparatedFile) {
            modelItems.forEach {
                createBasicClass(
                    isSeparatedFile = true,
                    modelItems = listOf(it)
                ).save(
                    srcDir = basicDirectory, fileName = "${it.className.toSnakeCase()}.dart"
                )
            }
        } else {
            createBasicClass(
                isSeparatedFile = false,
                modelItems = modelItems
            ).save(
                srcDir = basicDirectory, fileName = "${rootClassName.toSnakeCase()}.dart"
            )
        }
    }

    if(isCheckedDto && dtoDirectory != null) {
        if (isSeparatedFile) {
            modelItems.forEach {
                createDtoClass(
                    isSeparatedFile = true,
                    modelItems = listOf(it)
                ).save(
                    srcDir = dtoDirectory, fileName = "${it.className.toSnakeCase()}_dto.dart"
                )
            }
        } else {
            createDtoClass(
                isSeparatedFile = false,
                modelItems = modelItems
            ).save(
                srcDir = dtoDirectory, fileName = "${rootClassName.toSnakeCase()}_dto.dart"
            )
        }
    }

    if(isCheckedVo && voDirectory != null) {
        if (isSeparatedFile) {
            modelItems.forEach {
                createVoClass(
                    isSeparatedFile = true,
                    modelItems = listOf(it)
                ).save(
                    srcDir = voDirectory, fileName = "${it.className.toSnakeCase()}_vo.dart"
                )
            }
        } else {
            createVoClass(
                isSeparatedFile = false,
                modelItems = modelItems
            ).save(
                srcDir = voDirectory, fileName = "${rootClassName.toSnakeCase()}_vo.dart"
            )
        }
    }

    // Show the entered text in a message dialog
    Messages.showMessageDialog(
        "Your ClassName: $rootClassName", "Information", Messages.getInformationIcon()
    )
}