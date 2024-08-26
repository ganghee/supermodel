package com.github.ganghee.supermodel.extensions

import com.android.tools.idea.wizard.template.*
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.ide.progress.ModalTaskOwner.project
import com.intellij.psi.*
import org.jetbrains.kotlin.idea.*
import java.util.*


fun String.toSnakeCase() = replace(humps, "_").lowercase(Locale.getDefault())
private val humps = "(?<=.)(?=\\p{Upper})".toRegex()

fun String.save(srcDir: PsiDirectory, fileName: String) {
    try {
        WriteCommandAction.runWriteCommandAction(srcDir.project) {
            try {
                val newFile: VirtualFile =
                    srcDir.virtualFile.createChildData(this, fileName)
                newFile.setBinaryContent(this.toByteArray())
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }
    } catch (exc: Exception) {
        exc.printStackTrace()
    }
}

