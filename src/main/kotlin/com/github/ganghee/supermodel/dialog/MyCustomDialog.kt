package com.github.ganghee.supermodel.dialog

import com.github.ganghee.supermodel.create.createParameter
import com.github.ganghee.supermodel.model.ModelInfo
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.whenStateChangedFromUi
import com.intellij.ui.dsl.builder.whenTextChangedFromUi
import createClassOptionsPanel
import createHTML
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextArea


@Suppress("DialogTitleCapitalization", "UnstableApiUsage")
class MyCustomDialog : DialogWrapper(true) {
    private var objectName: String = ""
    private val models = mutableListOf<ModelInfo>()
    private var isSeparateCheckBoxSelected = false
    private var isFreezedSelected = false
    private var isFromJsonSelected = false
    private var isToJsonSelected = false

    init {
        title = "Json to Dart"
        init()
    }

    override fun createCenterPanel(): JComponent {
        val mainPanel = JPanel()
        val rightPanel = JPanel()
        val previewWidget = JLabel("Type something...").apply {
            font = java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12)
            minimumSize = Dimension(1000, 1000)
        }

        val jsonTextField = JTextArea(30, 70).apply {
            lineWrap = false
            wrapStyleWord = false
            font = java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12)
        }

        // separate file checkbox panel
        val separateCheckBoxPanel = panel {
            row {
                checkBox("Separate file").whenStateChangedFromUi { selected ->
                    isSeparateCheckBoxSelected = selected
                    createHTML(
                        modelItems = modelItems,
                        isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
                        previewWidget = previewWidget,
                        isFreezedSelected = isFreezedSelected,
                        isFromJsonSelected = isFromJsonSelected,
                        isToJsonSelected = isToJsonSelected
                    )
                }
            }
        }

        // 클래스 옵션 설정 panel
        val classOptionsPanel = JPanel()

        // 클래스 이름 입력 panel
        val classNameTextFieldPanel = panel {
            row {
                text("Class Name:")
                textField().whenTextChangedFromUi {
                    objectName = it

                    val changedModelItems = models.mapIndexed { index, modelInfo ->
                        if (index == 0) modelInfo.copy(className = it) else modelInfo
                    }
                    createHTML(
                        modelItems = changedModelItems,
                        isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
                        previewWidget = previewWidget,
                        isFreezedSelected = isFreezedSelected,
                        isFromJsonSelected = isFromJsonSelected,
                        isToJsonSelected = isToJsonSelected
                    )
                    createClassOptionsPanel(
                        classOptionsPanel = classOptionsPanel,
                        rightPanel = rightPanel,
                        models = changedModelItems,
                        isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
                        previewWidget = previewWidget,
                        isFreezedSelected = isFreezedSelected,
                        isFromJsonSelected = isFromJsonSelected,
                        isToJsonSelected = isToJsonSelected,
                        onCheckBoxClick = { isFreezedClick, isToJsonClick, isFromJsonClick ->
                            isFreezedSelected = isFreezedClick
                            isToJsonSelected = isToJsonClick
                            isFromJsonSelected = isFromJsonClick
                            createHTML(
                                modelItems = changedModelItems,
                                isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
                                previewWidget = previewWidget,
                                isFreezedSelected = isFreezedSelected,
                                isFromJsonSelected = isFromJsonSelected,
                                isToJsonSelected = isToJsonSelected
                            )
                        },
                    )
                }
            }
        }

        // json 입력 panel
        val jsonTextFieldPanel = panel {
            row {
                scrollCell(jsonTextField).align(AlignX.FILL)
            }
        }

        val previewPanel = panel {
            row {
                scrollCell(previewWidget).align(AlignX.FILL)
            }
        }

        jsonTextField.document.addDocumentListener(object : javax.swing.event.DocumentListener {
            override fun insertUpdate(e: javax.swing.event.DocumentEvent?) = updateText()
            override fun removeUpdate(e: javax.swing.event.DocumentEvent?) = updateText()
            override fun changedUpdate(e: javax.swing.event.DocumentEvent?) = updateText()

            private fun updateText() {
                val text = jsonTextField.text
                if (text.isNotEmpty()) {
                    try {
                        models.clear()
                        createParameter(
                            jsonText = text,
                            modelItems = models,
                            onParameter = { fields, parameters, imports ->
                                models.add(
                                    0, ModelInfo(
                                        className = objectName,
                                        fields = fields,
                                        parameters = parameters,
                                        imports = imports,
                                    )
                                )
                            }
                        )
                        createHTML(
                            modelItems = models,
                            isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
                            previewWidget = previewWidget,
                            isFreezedSelected = isFreezedSelected,
                            isFromJsonSelected = isFromJsonSelected,
                            isToJsonSelected = isToJsonSelected
                        )
                        createClassOptionsPanel(
                            classOptionsPanel = classOptionsPanel,
                            rightPanel = rightPanel,
                            models = models,
                            isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
                            previewWidget = previewWidget,
                            isFreezedSelected = isFreezedSelected,
                            isFromJsonSelected = isFromJsonSelected,
                            isToJsonSelected = isToJsonSelected,
                            onCheckBoxClick = { isFreezedClick, isToJsonClick, isFromJsonClick ->
                                isFreezedSelected = isFreezedClick
                                isToJsonSelected = isToJsonClick
                                isFromJsonSelected = isFromJsonClick
                                createHTML(
                                    modelItems = models,
                                    isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
                                    previewWidget = previewWidget,
                                    isFreezedSelected = isFreezedSelected,
                                    isFromJsonSelected = isFromJsonSelected,
                                    isToJsonSelected = isToJsonSelected
                                )
                            },
                        )

                    } catch (e: Exception) {
                        previewWidget.text = "wrong json format"
                    }
                } else {
                    previewWidget.text = "Type something..."
                }
            }
        })

        rightPanel.layout = BoxLayout(rightPanel, BoxLayout.Y_AXIS)
        rightPanel.add(classNameTextFieldPanel)
        rightPanel.add(separateCheckBoxPanel)
        rightPanel.add(classOptionsPanel)
        mainPanel.add(rightPanel)
        mainPanel.add(jsonTextFieldPanel)
        mainPanel.add(previewPanel)

        return mainPanel
    }


    val rootClassName: String
        get() = this.objectName

    val modelItems: List<ModelInfo>
        get() = this.models

    val isSeparatedFile: Boolean
        get() = this.isSeparateCheckBoxSelected
}