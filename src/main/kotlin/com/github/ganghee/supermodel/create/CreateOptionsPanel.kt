@file:Suppress("UnstableApiUsage")

import com.github.ganghee.supermodel.model.ModelInfo
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.whenStateChangedFromUi
import javax.swing.JLabel
import javax.swing.JPanel

fun createClassOptionsPanel(
    classOptionsPanel: JPanel,
    rightPanel: JPanel,
    models: List<ModelInfo>,
    isSeparateCheckBoxSelected: Boolean,
    previewWidget: JLabel,
    isFreezedSelected: Boolean,
    isFromJsonSelected: Boolean,
    isToJsonSelected: Boolean,
    onCheckBoxClick: (isFreezedSelected: Boolean, isToJsonSelected: Boolean, isFromJsonSelected: Boolean) -> Unit,
) {
    classOptionsPanel.removeAll()
    classOptionsPanel.add(
        panel {
            models.map {
                println("className: ${it.className}")
                row {
                    text("Class Name: ${it.className}")
                    checkBox("Create freezed").whenStateChangedFromUi { selected ->
                        onCheckBoxClick(selected, isToJsonSelected, isFromJsonSelected)
                        createHTML(
                            modelItems = models,
                            isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
                            previewWidget = previewWidget,
                            isFreezedSelected = isFreezedSelected,
                            isFromJsonSelected = isFromJsonSelected,
                            isToJsonSelected = isToJsonSelected
                        )
                    }
                    checkBox("Create toJson (request)").whenStateChangedFromUi { selected ->
                        onCheckBoxClick(isFreezedSelected, selected, isFromJsonSelected)
                        createHTML(
                            modelItems = models,
                            isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
                            previewWidget = previewWidget,
                            isFreezedSelected = isFreezedSelected,
                            isFromJsonSelected = isFromJsonSelected,
                            isToJsonSelected = isToJsonSelected
                        )
                    }
                    checkBox("Create fromJson (response)").whenStateChangedFromUi { selected ->
                        onCheckBoxClick(isFreezedSelected, isToJsonSelected, selected)
                        createHTML(
                            modelItems = models,
                            isSeparateCheckBoxSelected = isSeparateCheckBoxSelected,
                            previewWidget = previewWidget,
                            isFreezedSelected = isFreezedSelected,
                            isFromJsonSelected = isFromJsonSelected,
                            isToJsonSelected = isToJsonSelected
                        )
                    }
                }
            }
        }
    )
    // 아래 로직을 써야 preview가 갱신된다.
    classOptionsPanel.revalidate()
    classOptionsPanel.repaint()
    rightPanel.revalidate()
    rightPanel.repaint()
}