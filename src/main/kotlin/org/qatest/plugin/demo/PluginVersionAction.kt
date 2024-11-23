package org.qatest.plugin.demo

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import org.jetbrains.annotations.NotNull

class PopupDialogAction : AnAction() {
//    override fun update(@NotNull event: AnActionEvent) {
//        // Using the event, evaluate the context,
//        // and enable or disable the action.
//    }

    override fun actionPerformed(@NotNull event: AnActionEvent) {
        // Using the event, implement an action.
        // For example, create and show a dialog.
        Messages.showMessageDialog("QATestPlugin Message", "QATestPlugin", Messages.getInformationIcon())
    } // Override getActionUpdateThread() when you target 2022.3 or later!
}