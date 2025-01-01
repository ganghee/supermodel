package com.github.ganghee.supermodel

import com.github.ganghee.supermodel.create.createModelFile
import com.github.ganghee.supermodel.ui.DtoDirectory
import com.github.ganghee.supermodel.ui.MyCustomDialog
import com.github.ganghee.supermodel.ui.ResponseDirectory
import com.github.ganghee.supermodel.ui.VoDirectory
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager


class MyDemoAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val dialog = MyCustomDialog(project = e.project)

        val psiDirectory =
            if (e.getData(CommonDataKeys.PSI_ELEMENT) is PsiDirectory) e.getData(CommonDataKeys.PSI_ELEMENT) as PsiDirectory?
            else null

        if (psiDirectory != null) {
            if (dialog.showAndGet()) {
                val responsePsiDirectory =
                    getPsiDirectoryFromPath(e.project!!, ResponseDirectory.filePath)
                val dtoPsiDirectory = getPsiDirectoryFromPath(e.project!!, DtoDirectory.filePath)
                val voPsiDirectory = getPsiDirectoryFromPath(e.project!!, VoDirectory.filePath)

                if (ResponseDirectory.isCheck && responsePsiDirectory == null) {
                    Messages.showMessageDialog(
                        "Please select a directory for response",
                        "Information",
                        Messages.getInformationIcon()
                    )
                    return
                }
                if (DtoDirectory.isCheck && dtoPsiDirectory == null) {
                    Messages.showMessageDialog(
                        "Please select a directory for dto",
                        "Information",
                        Messages.getInformationIcon()
                    )
                    return
                }
                if (VoDirectory.isCheck && voPsiDirectory == null) {
                    Messages.showMessageDialog(
                        "Please select a directory for vo",
                        "Information",
                        Messages.getInformationIcon()
                    )
                    return
                }

                createModelFile(
                    basicDirectory = psiDirectory,
                    responsePsiDirectory = responsePsiDirectory,
                    dtoDirectory = dtoPsiDirectory,
                    voDirectory = voPsiDirectory,
                )
            }
        } else {
            Messages.showMessageDialog(
                "Please select a directory", "Information", Messages.getInformationIcon()
            )
        }
    }

    private fun getPsiDirectoryFromPath(project: Project, path: String): PsiDirectory? {
        val virtualFile =
            com.intellij.openapi.vfs.LocalFileSystem.getInstance().findFileByPath(path)
        return virtualFile?.let { file ->
            PsiManager.getInstance(project).findDirectory(file)
        }
    }
}
