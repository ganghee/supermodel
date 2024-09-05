package com.github.ganghee.supermodel.dialog

import com.github.ganghee.supermodel.create.createParameter
import com.github.ganghee.supermodel.model.ModelInfo
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.whenStateChangedFromUi
import com.intellij.ui.dsl.builder.whenTextChangedFromUi
import createHTML
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JTextArea


@Suppress("DialogTitleCapitalization", "UnstableApiUsage")
class MyCustomDialog : DialogWrapper(true) {
    private lateinit var objectName: String
    private val models = mutableListOf<ModelInfo>()
    private var isSeparateCheckBoxSelected = false

    init {
        title = "Json to Dart"
        init()
    }

    override fun createCenterPanel(): JComponent {

        val previewWidget = JLabel("Type something...").apply {
            font = java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12)
        }
        val jsonTextField = JTextArea(30, 70).apply {
            lineWrap = false
            wrapStyleWord = false
            font = java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12)
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
                                    0,
                                    ModelInfo(
                                        className = objectName,
                                        fields = fields,
                                        parameters = parameters,
                                        imports = imports,
                                    )
                                )
                            }
                        )
                        createHTML(
                            modelItems =modelItems,
                            isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
                            previewWidget = previewWidget
                        )
                    } catch (e: Exception) {
                        previewWidget.text = "wrong json format"
                    }
                }
            }
        })

        val panelDsl = panel {
            row {
                panel {
                    row {
                        text("Class Name:")
                        textField().whenTextChangedFromUi {
                            objectName = it
                            createHTML(
                                modelItems = modelItems,
                                isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
                                previewWidget = previewWidget
                            )
                        }
                    }
                    row {
                        checkBox("Separate file").whenStateChangedFromUi { selected ->
                            isSeparateCheckBoxSelected = selected
                            createHTML(
                                modelItems = modelItems,
                                isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
                                previewWidget = previewWidget
                            )
                        }
                    }
                    // 클래스 미리보기
                    row {
                        scrollCell(jsonTextField).align(AlignX.FILL)
                    }
                }

                cell(previewWidget).align(AlignX.CENTER)
            }
        }

        return panelDsl
    }

    val rootClassName: String
        get() = this.objectName

    val modelItems: List<ModelInfo>
        get() = this.models

    val isSeparatedFile: Boolean
        get() = this.isSeparateCheckBoxSelected
}