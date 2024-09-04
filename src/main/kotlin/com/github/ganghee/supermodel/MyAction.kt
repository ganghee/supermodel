package com.github.ganghee.supermodel

import com.github.ganghee.supermodel.create.createClassMessage
import com.github.ganghee.supermodel.create.createDataClassContent
import com.github.ganghee.supermodel.create.createParameter
import com.github.ganghee.supermodel.extensions.save
import com.github.ganghee.supermodel.extensions.toSnakeCase
import com.github.ganghee.supermodel.model.ModelInfo
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.whenTextChangedFromUi
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JTextArea


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
                createDataClass(
                    directory = psiDirectory,
                    rootClassName = dialog.rootClassName,
                    modelItems = dialog.modelItems
                )
            }
        } else {
            Messages.showMessageDialog(
                "Please select a directory", "Information", Messages.getInformationIcon()
            )
        }
    }

    private fun createDataClass(
        directory: PsiDirectory,
        rootClassName: String,
        modelItems: List<ModelInfo>
    ) {
        modelItems.forEach{
            createDataClassContent(
                modelItems = listOf(it)
            ).save(
                srcDir = directory, fileName = "${it.className.toSnakeCase()}.dart"
            )
        }
        // Show the entered text in a message dialog
        Messages.showMessageDialog(
            "Your ClassName: $rootClassName", "Information", Messages.getInformationIcon()
        )
    }
}

@Suppress("DialogTitleCapitalization", "UnstableApiUsage")
class MyCustomDialog : DialogWrapper(true) {
    private lateinit var objectName: String
    private var inputText: String = "{}"
    private val models = mutableListOf<ModelInfo>()

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
        val htmlJsons = mutableListOf<String>()

        jsonTextField.document.addDocumentListener(object : javax.swing.event.DocumentListener {
            override fun insertUpdate(e: javax.swing.event.DocumentEvent?) = updateText()
            override fun removeUpdate(e: javax.swing.event.DocumentEvent?) = updateText()
            override fun changedUpdate(e: javax.swing.event.DocumentEvent?) = updateText()

            private fun updateText() {
                val text = jsonTextField.text
                if (text.isNotEmpty()) {
                    try {
                        models.clear()
                        htmlJsons.clear()
                        createParameter(
                            jsonText = text,
                            modelItems = models,
                            onParameter = { fields, parameters ->
                                models.add(
                                    0,
                                    ModelInfo(
                                        className = objectName,
                                        fields = fields,
                                        parameters = parameters
                                    )
                                )
                            }
                        )
                        modelItems.forEach {
                            val htmlJsonText = createClassMessage(
                                className = it.className,
                                fields = it.fields.distinct(),
                                parameters = it.parameters.distinct()
                            )
                            htmlJsons.add(htmlJsonText)
                        }
                        previewWidget.text =  "<html>" + htmlJsons.joinToString("") + "</html>"
                        inputText = text
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
                            htmlJsons.clear()
                            modelItems.forEach {
                                val htmlJsonText = createClassMessage(
                                    className = rootClassName,
                                    fields = it.fields.distinct(),
                                    parameters = it.parameters.distinct()
                                )
                                htmlJsons.add(htmlJsonText)
                            }
                            previewWidget.text =
                                "<html>" + htmlJsons.joinToString("") + "</html>"
                        }
                    }
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
}