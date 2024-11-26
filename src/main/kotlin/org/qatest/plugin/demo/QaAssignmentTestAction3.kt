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

    override fun actionPerformed(e: AnActionEvent) {
        val selectedElement = e.getData(CommonDataKeys.NAVIGATABLE)
        val message = if (selectedElement != null) {
            "Selected UI Component:\n$selectedElement"
        } else {
            "No selected UI Component"
        }
        Messages.showMessageDialog(
            e.project,
            message,
            e.presentation.text,
            Messages.getInformationIcon()
        )
    }

    override fun update(e: AnActionEvent) {
        // Set the availability based on whether a project is open
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null
    }
}
