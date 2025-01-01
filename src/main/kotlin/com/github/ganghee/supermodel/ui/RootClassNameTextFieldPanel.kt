package com.github.ganghee.supermodel.ui

import com.github.ganghee.supermodel.model.Setting
import com.github.ganghee.supermodel.model.Setting.modelItems
import com.github.ganghee.supermodel.model.Setting.setModelItems
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.whenTextChangedFromUi
import createHTML
import javax.swing.JLabel

fun rootClassNameTextFieldPanel(previewWidget: JLabel): DialogPanel = panel {
    row {
        text("Class Name:")
        textField().whenTextChangedFromUi {
            Setting.setRootClassName(it)
            val models = modelItems.toMutableList()
            models.replaceAll { modelInfo ->
                if (modelInfo == modelItems.first()) {
                    modelInfo.copy(className = it)
                } else {
                    modelInfo
                }
            }
            setModelItems(models)

            createHTML(previewWidget = previewWidget)
        }
    }
}