package com.github.ganghee.supermodel.create

import com.github.ganghee.supermodel.extensions.save
import com.github.ganghee.supermodel.extensions.toSnakeCase
import com.github.ganghee.supermodel.model.Setting.isSeparatedFile
import com.github.ganghee.supermodel.model.Setting.modelItems
import com.github.ganghee.supermodel.model.Setting.rootClassName
import com.github.ganghee.supermodel.ui.DtoDirectory
import com.github.ganghee.supermodel.ui.ResponseDirectory
import com.github.ganghee.supermodel.ui.VoDirectory
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory

fun createModelFile(
    basicDirectory: PsiDirectory,
    responsePsiDirectory: PsiDirectory?,
    dtoDirectory: PsiDirectory?,
    voDirectory: PsiDirectory?,
) {
    if (ResponseDirectory.isCheck && responsePsiDirectory != null) {
        if (isSeparatedFile) {
            modelItems.forEach {
                createResponseClass(listOf(it)).save(
                    srcDir = responsePsiDirectory,
                    fileName = "${it.className.toSnakeCase()}_response.dart"
                )
            }
        } else {
            createResponseClass(modelItems).save(
                srcDir = responsePsiDirectory,
                fileName = "${rootClassName.toSnakeCase()}_response.dart"
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

    if (DtoDirectory.isCheck && dtoDirectory != null) {
        if (isSeparatedFile) {
            modelItems.forEach {
                createDtoClass(listOf(it)).save(
                    srcDir = dtoDirectory, fileName = "${it.className.toSnakeCase()}_dto.dart"
                )
            }
        } else {
            createDtoClass(modelItems).save(
                srcDir = dtoDirectory, fileName = "${rootClassName.toSnakeCase()}_dto.dart"
            )
        }
    }

    if (VoDirectory.isCheck && voDirectory != null) {
        if (isSeparatedFile) {
            modelItems.forEach {
                createVoClass(listOf(it)).save(
                    srcDir = voDirectory, fileName = "${it.className.toSnakeCase()}_vo.dart"
                )
            }
        } else {
            createVoClass(modelItems).save(
                srcDir = voDirectory, fileName = "${rootClassName.toSnakeCase()}_vo.dart"
            )
        }
    }

    // Show the entered text in a message dialog
    Messages.showMessageDialog(
        "Your ClassName: $rootClassName", "Information", Messages.getInformationIcon()
    )
}