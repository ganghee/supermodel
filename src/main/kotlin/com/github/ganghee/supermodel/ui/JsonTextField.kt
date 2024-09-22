package com.github.ganghee.supermodel.ui

import com.github.ganghee.supermodel.create.createClassOptionsPanel
import com.github.ganghee.supermodel.create.createParameter
import com.github.ganghee.supermodel.model.ModelInfo
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
    modelItems: MutableList<ModelInfo>,
    isCheckedResponse: Boolean,
    isSeparatedFile: Boolean,
    classOptionsPanel: JPanel,
    previewWidget: JLabel,
    leftPanel: JPanel,
    rootClassName: String,
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
            modelItems.clear()
            classOptionsPanel.isVisible = text.isNotEmpty() && !isCheckedResponse
            if (text.isNotEmpty()) {
                try {
                    createParameter(
                        jsonText = text,
                        modelItems = modelItems,
                        onParameter = { fields, parameters, imports ->
                            modelItems.add(
                                0, ModelInfo(
                                    className = rootClassName,
                                    fields = fields,
                                    parameters = parameters,
                                    imports = imports,
                                )
                            )
                        }
                    )
                    createHTML(
                        modelItems = modelItems,
                        isSeparateCheckBoxSelected = isSeparatedFile,
                        previewWidget = previewWidget,
                    )
                    createClassOptionsPanel(
                        classOptionsPanel = classOptionsPanel,
                        models = modelItems,
                        onCheckBoxClick = { index, isFreezed, isToJson, isFromJson ->
                            modelItems.replaceAll { modelInfo ->
                                val changedOptionModel = modelInfo.option.copy(
                                    isFreezedSelected = isFreezed
                                        ?: modelInfo.option.isFreezedSelected,
                                    isToJsonSelected = isToJson
                                        ?: modelInfo.option.isToJsonSelected,
                                    isFromJsonSelected = isFromJson
                                        ?: modelInfo.option.isFromJsonSelected,
                                )
                                if (modelInfo == modelItems[index]) {
                                    modelInfo.copy(option = changedOptionModel)
                                } else {
                                    modelInfo
                                }
                            }
                            createHTML(
                                modelItems = modelItems,
                                isSeparateCheckBoxSelected = isSeparatedFile,
                                previewWidget = previewWidget,
                            )

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
