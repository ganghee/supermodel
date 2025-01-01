package com.github.ganghee.supermodel.ui

import com.github.ganghee.supermodel.model.Setting.isSeparatedFile
import com.github.ganghee.supermodel.model.Setting.setSeparateFileState
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.AlignY
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.plus
import createHTML
import directoryPanel
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel


@Suppress("UnstableApiUsage")
class MyCustomDialog(
    val project: com.intellij.openapi.project.Project?
) : DialogWrapper(true) {

    init {
        title = "Json to Dart"
        init()
    }

    override fun createCenterPanel(): JComponent {
        val mainPanel = JPanel()
        val leftPanel = JPanel().apply {
            minimumSize = Dimension(600, 500)
        }
        val previewWidget = JLabel("Type something...").apply {
            font = java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12)
            maximumSize = Dimension(100, 400)
        }
        // 클래스 옵션 설정 panel
        val classOptionsPanel = JPanel()

        leftPanel.apply {
            layout = BoxLayout(leftPanel, BoxLayout.Y_AXIS)
            alignmentX = 0.5f
            alignmentY = 0.5f

            // separate file checkbox panel
            add(
                panel {
                    row {
                        checkBox("Separate file").bindSelected(
                            { isSeparatedFile },
                            {}
                        ).onChanged {
                            setSeparateFileState(!isSeparatedFile)
                            createHTML(previewWidget = previewWidget)
                        }.align(AlignY.FILL + AlignX.LEFT)
                    }
                }
            )
            // 클래스 이름 입력 panel
            add(
                rootClassNameTextFieldPanel(previewWidget = previewWidget)
            )
            // response 파일 이름 입력 panel
            add(
                directoryPanel(
                    project = project,
                    directoryType = ResponseDirectory,
                    onVisibleClassOptionsPanel = { isResponseCheck ->
                        classOptionsPanel.isVisible = !isResponseCheck
                    }
                )
            )
            // dto 파일 이름 입력 panel
            add(
                directoryPanel(
                    project = project,
                    directoryType = DtoDirectory,
                )
            )
            // vo 파일 이름 입력 panel
            add(
                directoryPanel(
                    project = project,
                    directoryType = VoDirectory,
                )
            )
            add(classOptionsPanel)
        }
        mainPanel.apply {
            add(leftPanel)
            // json 입력 panel, preview panel
            add(
                panel {
                    row {
                        scrollCell(
                            jsonTextField(
                                project = project,
                                classOptionsPanel = classOptionsPanel,
                                previewWidget = previewWidget,
                                leftPanel = leftPanel,
                            )
                        ).align(AlignX.FILL)
                    }
                    row {
                        scrollCell(previewWidget).align(AlignX.FILL).enabled(true).align(AlignY.TOP)
                    }
                }
            )
        }

        return mainPanel
    }
}