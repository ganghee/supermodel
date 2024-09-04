package com.github.ganghee.supermodel

import com.github.ganghee.supermodel.create.createModelFile
import com.github.ganghee.supermodel.dialog.MyCustomDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory


class MyDemoAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val dialog = MyCustomDialog()

        val psiDirectory =
            if (e.getData(CommonDataKeys.PSI_ELEMENT) is PsiDirectory) e.getData(CommonDataKeys.PSI_ELEMENT) as PsiDirectory?
            else null

        if (psiDirectory != null) {
            val virtualFile = psiDirectory.virtualFile
            val directoryPath = virtualFile.path

            if (dialog.showAndGet()) {
                createModelFile(
                    directory = psiDirectory,
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
}
