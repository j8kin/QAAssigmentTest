package org.qatest.plugin.demo

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.ui.Messages
import org.jetbrains.annotations.NotNull

class PluginVersionAction : AnAction() {
    override fun actionPerformed(@NotNull event: AnActionEvent) {
        val pluginVer = PluginManagerCore.getPlugin(PluginId.getId("org.qatest.plugin.demo"))?.version
        Messages.showMessageDialog("version: $pluginVer", "QATestPlugin version", Messages.getInformationIcon())
    }
}