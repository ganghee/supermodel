package com.github.ganghee.supermodel.ui

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.AlignY
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.text
import java.awt.Dimension

// response 파일 이름 입력 panel
fun directoryPanel(
    project: Project?,
    directoryType: DirectoryState,
    onVisibleClassOptionsPanel: ((isResponseCheck: Boolean) -> Unit)? = null
): DialogPanel {
    var fileButton: Panel? = null
    return panel {
        row {
            label("${directoryType.className} Directory:").align(AlignY.TOP)
            panel {
                panel {
                    row {
                        checkBox("create ${directoryType.className} Model").bindSelected(
                            { directoryType.isCheck },
                            {}
                        ).onChanged {
                            directoryType.check(!directoryType.isCheck)
                            onVisibleClassOptionsPanel?.invoke(directoryType.isCheck)
                            fileButton?.visible(directoryType.isCheck)
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
                                directoryType.changeFilePath(file.path)
                                file.path
                            }
                        ).apply {
                            text(directoryType.filePath)
                        }
                    }
                }.visible(directoryType.isCheck)
            }.align(AlignY.TOP)
        }
    }.apply {
        preferredSize = Dimension(400, 100)
    }
}