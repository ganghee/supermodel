package com.github.ganghee.supermodel.ui

import com.github.ganghee.supermodel.model.ModelInfo
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.AlignY
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.plus
import com.intellij.ui.dsl.builder.whenStateChangedFromUi
import createHTML
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel


@Suppress("UnstableApiUsage")
class MyCustomDialog(
    val project: com.intellij.openapi.project.Project?
) : DialogWrapper(true) {

    private var _isCheckedResponse = false
    val isCheckedResponse: Boolean
        get() = this._isCheckedResponse

    private var _isCheckedDto = false
    val isCheckedDto: Boolean
        get() = this._isCheckedDto

    private var _isCheckedVo = false
    val isCheckedVo: Boolean
        get() = this._isCheckedVo

    private var _responseDirectory: String = ""
    val responseDirectory: String
        get() = this._responseDirectory

    private var _dtoDirectory: String = ""
    val dtoDirectory: String
        get() = this._dtoDirectory

    private var _voDirectory: String = ""
    val voDirectory: String
        get() = this._voDirectory

    private var _rootClassName: String = ""
    val rootClassName: String
        get() = this._rootClassName

    private val _modelItems = mutableListOf<ModelInfo>()
    val modelItems: List<ModelInfo>
        get() = this._modelItems

    private var _isSeparatedFile = false
    val isSeparatedFile: Boolean
        get() = this._isSeparatedFile

    init {
        title = "Json to Dart"
        init()
    }

    override fun createCenterPanel(): JComponent {
        val mainPanel = JPanel()
        val leftPanel = JPanel().apply {
            minimumSize = Dimension(1000, 500)
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
                        checkBox("Separate file").whenStateChangedFromUi { selected ->
                            _isSeparatedFile = selected
                            createHTML(
                                modelItems = _modelItems,
                                isSeparateCheckBoxSelected = _isSeparatedFile,
                                previewWidget = previewWidget,
                            )
                        }.align(AlignY.FILL + AlignX.LEFT)
                    }
                }
            )
            // 클래스 이름 입력 panel
            add(
                rootClassNameTextFieldPanel(
                    models = _modelItems,
                    isSeparateCheckBoxSelected = _isSeparatedFile,
                    previewWidget = previewWidget,
                    onChangedRootClassName = { changedClassName ->
                        _rootClassName = changedClassName
                    },
                )
            )
            // response 파일 이름 입력 panel
            add(
                directoryPanel(
                    project = project,
                    directoryType = DirectoryType.RESPONSE,
                    onChangedCheckBox = { selected ->
                        _isCheckedResponse = selected
                        classOptionsPanel.isVisible = !selected
                    },
                    onChangedFilePath = { path -> _responseDirectory = path },
                )
            )
            // dto 파일 이름 입력 panel
            add(
                directoryPanel(
                    project = project,
                    directoryType = DirectoryType.DTO,
                    onChangedCheckBox = { selected -> _isCheckedDto = selected },
                    onChangedFilePath = { path -> _dtoDirectory = path },
                )
            )
            // vo 파일 이름 입력 panel
            add(
                directoryPanel(
                    project = project,
                    directoryType = DirectoryType.VO,
                    onChangedCheckBox = { selected -> _isCheckedVo = selected },
                    onChangedFilePath = { path -> _voDirectory = path },
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
                                modelItems = _modelItems,
                                isCheckedResponse = _isCheckedResponse,
                                isSeparatedFile = _isSeparatedFile,
                                classOptionsPanel = classOptionsPanel,
                                previewWidget = previewWidget,
                                leftPanel = leftPanel,
                                rootClassName = _rootClassName,
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