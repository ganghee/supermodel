package com.github.ganghee.supermodel.ui

import com.github.ganghee.supermodel.model.ModelInfo
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.whenTextChangedFromUi
import createHTML
import javax.swing.JLabel

fun rootClassNameTextFieldPanel(
    models: MutableList<ModelInfo>,
    isSeparateCheckBoxSelected: Boolean,
    previewWidget: JLabel,
    onChangedRootClassName: (String) -> Unit,
): DialogPanel = panel {
    row {
        text("Class Name:")
        textField().whenTextChangedFromUi {
            onChangedRootClassName(it)
            models.replaceAll { modelInfo ->
                if (modelInfo == models.first()) {
                    modelInfo.copy(className = it)
                } else {
                    modelInfo
                }
            }
            createHTML(
                modelItems = models,
                isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
                previewWidget = previewWidget,
            )
        }
    }
}