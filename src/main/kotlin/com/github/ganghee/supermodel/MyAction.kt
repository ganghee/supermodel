package com.github.ganghee.supermodel

import com.github.ganghee.supermodel.create.createModelFile
import com.github.ganghee.supermodel.ui.MyCustomDialog
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
                val responsePath = dialog.responseDirectory
                val responsePsiDirectory = getPsiDirectoryFromPath(e.project!!, responsePath)
                val dtoPath = dialog.dtoDirectory
                val dtoPsiDirectory = getPsiDirectoryFromPath(e.project!!, dtoPath)
                val voPath = dialog.voDirectory
                val voPsiDirectory = getPsiDirectoryFromPath(e.project!!, voPath)

                if(dialog.isCheckedResponse && responsePsiDirectory == null) {
                    Messages.showMessageDialog(
                        "Please select a directory for response", "Information", Messages.getInformationIcon()
                    )
                    return
                }
                if(dialog.isCheckedDto && dtoPsiDirectory == null) {
                    Messages.showMessageDialog(
                        "Please select a directory for dto", "Information", Messages.getInformationIcon()
                    )
                    return
                }
                if(dialog.isCheckedVo && voPsiDirectory == null) {
                    Messages.showMessageDialog(
                        "Please select a directory for vo", "Information", Messages.getInformationIcon()
                    )
                    return
                }

                createModelFile(
                    basicDirectory = psiDirectory,
                    isCheckedResponse = dialog.isCheckedResponse,
                    isCheckedDto = dialog.isCheckedDto,
                    isCheckedVo = dialog.isCheckedVo,
                    responsePsiDirectory = responsePsiDirectory,
                    dtoDirectory = dtoPsiDirectory,
                    voDirectory = voPsiDirectory,
                    rootClassName = dialog.rootClassName,
                    modelItems = dialog.modelItems,
                    isSeparatedFile = dialog.isSeparatedFile
                )
            }
        } else {
            Messages.showMessageDialog(
                "Please select a directory", "Information", Messages.getInformationIcon()
            )
        }
    }

    private fun getPsiDirectoryFromPath(project: Project, path: String): PsiDirectory? {
        val virtualFile = com.intellij.openapi.vfs.LocalFileSystem.getInstance().findFileByPath(path)
        return virtualFile?.let { file ->
            PsiManager.getInstance(project).findDirectory(file)
        }
    }
}
