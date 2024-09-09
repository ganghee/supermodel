package com.github.ganghee.supermodel.extensions

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import java.util.Locale


fun String.toSnakeCase() = replace(humps, "_").lowercase(Locale.getDefault())

fun String.toUpperCamelCase() = split("_").joinToString("") {
    it.replaceFirstChar { word ->
        if (word.isLowerCase()) word.titlecase(
            Locale.getDefault()
        ) else word.toString()
    }
}

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

fun String.addSuffix(suffix: String, type: String? = null): String {
    val isPrimitive = this in listOf("int", "double", "String", "bool")
    val isTypeObject = type !in listOf("int", "double", "String", "bool")
    if((!isPrimitive && type == null) || (type != null && isTypeObject)) {
        return this + suffix
    }
    return this;
}