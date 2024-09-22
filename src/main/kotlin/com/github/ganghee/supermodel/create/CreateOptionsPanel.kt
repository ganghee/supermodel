package com.github.ganghee.supermodel.create

import com.github.ganghee.supermodel.model.Setting.modelItems
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.whenStateChangedFromUi
import javax.swing.JPanel

fun createClassOptionsPanel(
    classOptionsPanel: JPanel,
    onCheckBoxClick: (index: Int, isFreezed: Boolean?, isToJson: Boolean?, isFromJson: Boolean?) -> Unit,
) {
    classOptionsPanel.removeAll()
    classOptionsPanel.add(
        panel {
            modelItems.mapIndexed { index, modelInfo ->
                row {
                    text("Class Name: ${modelInfo.className}")
                }
                row {
                    checkBox("Create freezed").whenStateChangedFromUi { selected ->
                        onCheckBoxClick(index, selected, null, null)
                    }
                    checkBox("Create toJson (request)").whenStateChangedFromUi { selected ->
                        onCheckBoxClick(index, null, selected, null)
                    }
                    checkBox("Create fromJson (response)").whenStateChangedFromUi { selected ->
                        onCheckBoxClick(index, null, null, selected)
                    }
                }
            }
        }
    )
}