package com.github.ganghee.supermodel.dialog

import com.github.ganghee.supermodel.create.createClassOptionsPanel
import com.github.ganghee.supermodel.create.createParameter
import com.github.ganghee.supermodel.model.ModelInfo
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.fileTypes.PlainTextLanguage
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.AlignY
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.plus
import com.intellij.ui.dsl.builder.whenStateChangedFromUi
import com.intellij.ui.dsl.builder.whenTextChangedFromUi
import createHTML
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextArea


@Suppress("DialogTitleCapitalization", "UnstableApiUsage")
class MyCustomDialog(
    val project: com.intellij.openapi.project.Project?
) : DialogWrapper(true) {
    private var objectName: String = ""
    private var responseDirectory: String = ""
    private var dtoDirectory: String = ""
    private var voDirectory: String = ""
    private val models = mutableListOf<ModelInfo>()
    private var isSeparateCheckBoxSelected = false
    private var isResponseChecked = false
    private var isDtoChecked = false
    private var isVoChecked = false

    init {
        title = "Json to Dart"
        init()
    }

    override fun createCenterPanel(): JComponent {
        val mainPanel = JPanel()
        val leftPanel = JPanel().apply {
            minimumSize = Dimension(1000, 500)
        }
        var responseFileButton: Panel? = null
        var dtoFileButton: Panel? = null
        var voFileButton: Panel? = null
        val previewWidget = JLabel("Type something...").apply {
            font = java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12)
            maximumSize = Dimension(100, 400)
        }

        val editorTextField = CustomEditorField(PlainTextLanguage.INSTANCE, project, "")
        editorTextField.setOneLineMode(false)
        editorTextField.preferredSize = Dimension(800, 600)

        editorTextField.isVisible = true


        // separate file checkbox panel
        val separateCheckBoxPanel = panel {
            row {
                checkBox("Separate file").whenStateChangedFromUi { selected ->
                    isSeparateCheckBoxSelected = selected
                    createHTML(
                        modelItems = models,
                        isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
                        previewWidget = previewWidget,
                    )
                }.align(AlignY.FILL + AlignX.LEFT)
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
                    models.replaceAll { modelInfo ->
                        if (modelInfo == models.first()) {
                            modelInfo.copy(className = objectName)
                        } else {
                            modelInfo
                        }
                    }
                    createHTML(
                        modelItems = models,
                        isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
                        previewWidget = previewWidget,
                    )
                }
            }
        }

        // response 파일 이름 입력 panel
        val responseDirectoryPanel = panel {
            row {
                label("Response Directory:").align(AlignY.TOP)
                panel {
                    panel {
                        row {
                            checkBox("create Response Model").whenStateChangedFromUi { selected ->
                                isResponseChecked = selected
                                responseFileButton?.visible(selected)
                                classOptionsPanel.isVisible = !selected
                            }
                        }
                    }.align(AlignY.FILL)
                    // response 파일 경로 입력 panel
                    responseFileButton = panel {
                        row {
                            textFieldWithBrowseButton(
                                project = project,
                                fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor(),
                                fileChosen = { file ->
                                    responseDirectory = file.path
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

        // dto 파일 이름 입력 panel
        val dtoDirectoryPanel = panel {
            row {
                label("Dto Directory:").align(AlignY.TOP)
                panel {
                    panel {
                        row {
                            checkBox("create Dto Model").whenStateChangedFromUi { selected ->
                                isDtoChecked = selected
                                dtoFileButton?.visible(selected)
                            }
                        }
                    }.align(AlignY.FILL)
                    // dto 파일 경로 입력 panel
                    dtoFileButton = panel {
                        row {
                            textFieldWithBrowseButton(
                                project = project,
                                fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor(),
                                fileChosen = { file ->
                                    dtoDirectory = file.path
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

        // vo 파일 이름 입력 panel
        val voDirectoryPanel = panel {
            row {
                label("VO Directory:").align(AlignY.TOP)
                panel {
                    panel {
                        row {
                            checkBox("create VO Model").whenStateChangedFromUi { selected ->
                                isVoChecked = selected
                                voFileButton?.visible(selected)
                            }
                        }
                    }.align(AlignY.FILL)
                    // vo 파일 경로 입력 panel
                    voFileButton = panel {
                        row {
                            textFieldWithBrowseButton(
                                project = project,
                                fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor(),
                                fileChosen = { file ->
                                    voDirectory = file.path
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

        // json 입력 panel, preview panel
        val jsonTextFieldPanel = panel {
            row {
                scrollCell(editorTextField).align(AlignX.FILL)
            }
            row {
                scrollCell(previewWidget).align(AlignX.FILL).enabled(true).align(AlignY.TOP)
            }
        }

        editorTextField.document.addDocumentListener(object: DocumentListener {

            override fun documentChanged(event: DocumentEvent) {
                updateText()
            }

            override fun beforeDocumentChange(event: DocumentEvent) {
                updateText()
            }

            private fun updateText() {
                val text = editorTextField.text
                models.clear()
                classOptionsPanel.isVisible = text.isNotEmpty() && !isResponseChecked
                if (text.isNotEmpty()) {
                    try {
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
                        )
                        createClassOptionsPanel(
                            classOptionsPanel = classOptionsPanel,
                            models = models,
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
                                createHTML(
                                    modelItems = models,
                                    isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
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

        leftPanel.apply {
            layout = BoxLayout(leftPanel, BoxLayout.Y_AXIS)
            alignmentX = 0.5f
            alignmentY = 0.5f
            add(separateCheckBoxPanel)
            add(classNameTextFieldPanel)
            add(responseDirectoryPanel)
            add(dtoDirectoryPanel)
            add(voDirectoryPanel)
            add(classOptionsPanel)
        }
        mainPanel.apply {
            add(leftPanel)
            add(jsonTextFieldPanel)
        }

        return mainPanel
    }

    val isCheckedResponse: Boolean
        get() = this.isResponseChecked

    val isCheckedDto: Boolean
        get() = this.isDtoChecked

    val isCheckedVo: Boolean
        get() = this.isVoChecked


    val selectedResponseDirectory: String
        get() = this.responseDirectory

    val selectedDtoDirectory: String
        get() = this.dtoDirectory

    val selectedVoDirectory: String
        get() = this.voDirectory

    val rootClassName: String
        get() = this.objectName

    val modelItems: List<ModelInfo>
        get() = this.models

    val isSeparatedFile: Boolean
        get() = this.isSeparateCheckBoxSelected
}