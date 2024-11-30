package org.qatest.plugin.demo

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.ui.Messages

class QaAssignmentTestAction1 : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        var pluginVersion = PluginManagerCore.getPlugins().first { plugin -> plugin.name == "Kotlin" }.version
        pluginVersion = if (pluginVersion == null) {
            "No Kotlin plugin installed"
        } else {
            "Kotlin Plugin version: $pluginVersion"
        }
        Messages.showMessageDialog(
            pluginVersion, e.presentation.text, Messages.getInformationIcon()
        )
    }
}