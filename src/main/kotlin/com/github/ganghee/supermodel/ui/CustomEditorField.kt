package com.github.ganghee.supermodel.ui

import com.intellij.lang.Language
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.project.Project
import com.intellij.ui.LanguageTextField

class CustomEditorField(language: Language, project: Project?, s: String) : LanguageTextField(language, project, s) {

    override fun createEditor(): EditorEx {
        val editor = super.createEditor()
        editor.setVerticalScrollbarVisible(true)
        editor.setHorizontalScrollbarVisible(true)

        val settings = editor.settings
        settings.isLineNumbersShown = true
        settings.isAutoCodeFoldingEnabled = true
        settings.isFoldingOutlineShown = true
        settings.isAllowSingleLogicalLineFolding = true
        settings.isRightMarginShown=true
        return editor
    }
}