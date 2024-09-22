package com.github.ganghee.supermodel.ui

import com.github.ganghee.supermodel.extensions.toUpperCamelCase
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.AlignY
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.whenStateChangedFromUi
import java.awt.Dimension

enum class DirectoryType {
    RESPONSE, DTO, VO
}

// response 파일 이름 입력 panel
fun directoryPanel(
    project: Project?,
    directoryType: DirectoryType,
    onChangedCheckBox: (Boolean) -> Unit,
    onChangedFilePath: (String) -> Unit
): DialogPanel {
    var fileButton: Panel? = null
    return panel {
        row {
            label("${directoryType.name.toUpperCamelCase()} Directory:").align(AlignY.TOP)
            panel {
                panel {
                    row {
                        checkBox("create ${directoryType.name.toUpperCamelCase()} Model").whenStateChangedFromUi { selected ->
                            onChangedCheckBox(selected)
                            fileButton?.visible(selected)
                        }
                    }
                }.align(AlignY.FILL)
                // response 파일 경로 입력 panel
                fileButton = panel {
                    row {
                        textFieldWithBrowseButton(
                            project = project,
                            fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor(),
                            fileChosen = { file ->
                                onChangedFilePath(file.path)
                                file.path
                            }
                        )
                    }
                }.visible(false)
            }.align(AlignY.TOP)
        }
    }.apply {
        preferredSize = Dimension(400, 100)
    }
}