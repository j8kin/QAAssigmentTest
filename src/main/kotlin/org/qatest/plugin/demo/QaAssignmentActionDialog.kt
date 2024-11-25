package org.qatest.plugin.demo

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.Messages

class QaAssignmentActionDialog : AnAction() {
    private val pluginView = PluginCreateViewPanel()

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        if (project != null) {
            val panel = pluginView.createView(project)
            val dialog = DialogBuilder()
            dialog.setCenterPanel(panel)
            dialog.show()
        }
        else {
            // this branch should never be reached
            Messages.showMessageDialog("There are no active project","UI Info Plugin", Messages.getErrorIcon())
        }
    }
}