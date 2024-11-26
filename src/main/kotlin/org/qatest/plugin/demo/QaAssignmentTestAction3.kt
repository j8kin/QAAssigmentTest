package org.qatest.plugin.demo

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages

class QaAssignmentTestAction3 : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun actionPerformed(event: AnActionEvent) {
        val selectedElement = event.getData(CommonDataKeys.NAVIGATABLE)
        val message = if (selectedElement != null) {
            "Selected UI Component:\n$selectedElement"
        } else {
            "No selected UI Component"
        }
        Messages.showMessageDialog(
            event.project,
            message,
            event.presentation.text,
            Messages.getInformationIcon()
        )
    }

    override fun update(event: AnActionEvent) {
        // Set the availability based on whether a project is open
        val project = event.project
        event.presentation.isEnabledAndVisible = project != null
    }
}
