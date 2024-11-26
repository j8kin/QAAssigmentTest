package org.qatest.plugin.demo

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.ui.Messages

class QaAssignmentTestAction1 : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        var pluginVersion = PluginManagerCore.getPlugin(PluginId.getId("org.qatest.plugin.demo"))?.version
        pluginVersion = if (pluginVersion == null) {
            "No plugin version"
        } else {
            "Plugin version: $pluginVersion"
        }
        Messages.showMessageDialog(
            pluginVersion,
            e.presentation.text,
            Messages.getInformationIcon()
        )
    }
}