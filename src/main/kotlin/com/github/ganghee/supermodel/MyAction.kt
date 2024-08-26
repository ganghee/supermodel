package com.github.ganghee.supermodel

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.ganghee.supermodel.create.createClassMessage
import com.github.ganghee.supermodel.extensions.save
import com.github.ganghee.supermodel.extensions.toSnakeCase
import com.github.ganghee.supermodel.model.createModel
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
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

        // Check if a directory is selected
        if (psiDirectory != null) {
            val virtualFile = psiDirectory.virtualFile
            val directoryPath = virtualFile.path

            if (dialog.showAndGet()) {
                createDataClass(
                    directory = psiDirectory,
                    className = dialog.className,
                    directoryPath = directoryPath,
                    project = e.project!!,
                    jsonText = dialog.jsonText
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
        className: String,
        directoryPath: String,
        project: Project,
        jsonText: String
    ) {
        createModel(
            packageName = directoryPath,
            className = className,
            project = project,
            jsonText = jsonText
        ).save(
            srcDir = directory, fileName = "${className.toSnakeCase()}.dart"
        )
        // Show the entered text in a message dialog
        Messages.showMessageDialog(
            "Your ClassName: $className", "Information", Messages.getInformationIcon()
        )
    }
}

@Suppress("DialogTitleCapitalization", "UnstableApiUsage")
class MyCustomDialog(
) : DialogWrapper(true) {
    private lateinit var objectName: String
    private var inputText: String = "{}"

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
        lateinit var jsonText: String
        var fields: List<String> = listOf(
            "final int? memberSeq;",
            "final String? memberNick;",
        )
        var parameters: List<String> = listOf(
            "required this.memberSeq,",
            "required this.memberNick,",
        )

        jsonTextField.document.addDocumentListener(object : javax.swing.event.DocumentListener {
            override fun insertUpdate(e: javax.swing.event.DocumentEvent?) = updateText()
            override fun removeUpdate(e: javax.swing.event.DocumentEvent?) = updateText()
            override fun changedUpdate(e: javax.swing.event.DocumentEvent?) = updateText()

            private fun updateText() {
                val text = jsonTextField.text
                if (text.isNotEmpty()) {
                    try {
                        jsonText = createClassMessage(
                            className = objectName,
                            fields = fields,
                            parameters = parameters
                        )
                        previewWidget.text = jsonText
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
                            jsonText = createClassMessage(
                                className = objectName,
                                fields = fields,
                                parameters = parameters
                            )
                            previewWidget.text = jsonText
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

    val className: String
        get() = this.objectName

    val jsonText: String
        get() = this.inputText
}