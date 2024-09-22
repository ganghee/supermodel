package com.github.ganghee.supermodel.ui

import com.github.ganghee.supermodel.create.createClassOptionsPanel
import com.github.ganghee.supermodel.create.createParameter
import com.github.ganghee.supermodel.model.ModelInfo
import com.github.ganghee.supermodel.model.Setting.modelItems
import com.github.ganghee.supermodel.model.Setting.rootClassName
import com.github.ganghee.supermodel.model.Setting.setModelItems
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.fileTypes.PlainTextLanguage
import com.intellij.openapi.project.Project
import createHTML
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.JPanel

fun jsonTextField(
    project: Project?,
    classOptionsPanel: JPanel,
    previewWidget: JLabel,
    leftPanel: JPanel,
): CustomEditorField {

    val jsonTextField = CustomEditorField(PlainTextLanguage.INSTANCE, project, "")
    jsonTextField.setOneLineMode(false)
    jsonTextField.preferredSize = Dimension(800, 600)
    jsonTextField.isVisible = true

    jsonTextField.document.addDocumentListener(object : DocumentListener {

        override fun documentChanged(event: DocumentEvent) {
            updateText()
        }

        override fun beforeDocumentChange(event: DocumentEvent) {
            updateText()
        }

        private fun updateText() {
            val text = jsonTextField.text
            val models = modelItems.toMutableList()

            models.clear()
            classOptionsPanel.isVisible = text.isNotEmpty() && !ResponseDirectory.isCheck
            if (text.isNotEmpty()) {
                try {
                    createParameter(
                        jsonText = text,
                        modelItems = models,
                        onParameter = { fields, parameters, imports ->
                            models.add(
                                0, ModelInfo(
                                    className = rootClassName,
                                    fields = fields,
                                    parameters = parameters,
                                    imports = imports,
                                )
                            )
                        }
                    )
                    setModelItems(models)
                    createHTML(previewWidget = previewWidget)
                    createClassOptionsPanel(
                        classOptionsPanel = classOptionsPanel,
                        onCheckBoxClick = { index, isFreezed, isToJson, isFromJson ->
                            models.replaceAll { modelInfo ->
                                val changedOptionModel = modelInfo.option.copy(
                                    isFreezedSelected = isFreezed
                                        ?: modelInfo.option.isFreezedSelected,
                                    isToJsonSelected = isToJson
                                        ?: modelInfo.option.isToJsonSelected,
                                    isFromJsonSelected = isFromJson
                                        ?: modelInfo.option.isFromJsonSelected,
                                )
                                if (modelInfo == models[index]) {
                                    modelInfo.copy(option = changedOptionModel)
                                } else {
                                    modelInfo
                                }
                            }
                            createHTML(previewWidget = previewWidget)

                        },
                    )
                } catch (e: Exception) {
                    previewWidget.text = "wrong json format"
                }
            } else {
                previewWidget.text = "Type something..."
            }
            // 아래 로직을 써야 preview가 갱신된다.
            classOptionsPanel.revalidate()
            classOptionsPanel.repaint()
            leftPanel.revalidate()
            leftPanel.repaint()
        }
    })
    return jsonTextField
}
