package org.qatest.plugin.demo

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.FocusWatcher
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.impl.IdeFrameImpl
import com.intellij.ui.content.ContentFactory
import java.awt.AWTEvent
import java.awt.BorderLayout
import java.awt.Component
import java.awt.event.ActionEvent
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.JButton
import javax.swing.SwingUtilities

internal class QaAssignmentTestToolWindowFactory : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val toolWindowContent = UiInfoToolWindowContent(project, toolWindow)
        val content = ContentFactory.getInstance().createContent(toolWindowContent.contentPanel, "", false)

        toolWindow.contentManager.addContent(content)
    }

    private inner class UiInfoToolWindowContent(val project: Project, toolWindow: ToolWindow) {
        val contentPanel: JPanel = JPanel()
        private val pluginView = PluginCreateViewPanel()
        private val cUIInfo = JLabel()

        init {
            val comp = (IdeFrameImpl.activeFrame as IdeFrameImpl).component
            if (comp != null) {
                object : FocusWatcher() {
                    override fun focusedComponentChanged(focusedComponent: Component?, cause: AWTEvent?) {
                        if (focusedComponent != null && SwingUtilities.isDescendingFrom(
                                focusedComponent,
                                toolWindow.component
                            )
                        ) {
                            //refresh table here
                            cUIInfo.text = "Focus Gained"
                        }
                    }
                }.install(comp)
            }
            updateUiInfoMainPanel(toolWindow, null)
        }

        fun createControlsPanel(toolWindow: ToolWindow): JPanel {
            val controlsPanel = JPanel()
            val refreshButton = JButton("Refresh")
            refreshButton.addActionListener { e: ActionEvent -> updateUiInfoMainPanel(toolWindow, e) }
            controlsPanel.add(refreshButton)
            val hideToolWindowButton = JButton("Hide")
            hideToolWindowButton.addActionListener { toolWindow.hide(null) }
            controlsPanel.add(hideToolWindowButton)
            return controlsPanel
        }

        fun updateUiInfoMainPanel(toolWindow: ToolWindow, event: ActionEvent?) {
            contentPanel.removeAll()

            pluginView.createView(project).components.forEach { component ->
                contentPanel.add(component)
            }

            contentPanel.add(createControlsPanel(toolWindow), BorderLayout.CENTER)
            contentPanel.validate()
            contentPanel.repaint()
        }
    }
}
