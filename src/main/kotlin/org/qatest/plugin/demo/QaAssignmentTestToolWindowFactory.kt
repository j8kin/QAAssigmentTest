package org.qatest.plugin.demo

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.editor.ex.EditorGutterComponentEx
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.table.JBTable
import java.awt.BorderLayout
import javax.swing.*
import javax.swing.table.DefaultTableModel

internal class QaAssignmentTestToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val toolWindowContent = UiInfoToolWindowContent(project, toolWindow)
        val content = ContentFactory.getInstance().createContent(toolWindowContent.contentPanel, "", false)

        toolWindow.contentManager.addContent(content)
    }

    private class UiInfoToolWindowContent(val project: Project, toolWindow: ToolWindow) {
        val contentPanel: JPanel = JPanel()
        private val cUIInfo = JLabel()

        init {
            updateUiInfoMainPanel(toolWindow)
        }

        fun getActiveFile(): VirtualFile? {
            return (FileEditorManager.getInstance(project) as FileEditorManagerImpl).currentFile
        }

        fun gutterModelTable(row: Int, col: Int): DefaultTableModel = object : DefaultTableModel(row, col) {
            //  Returning the Icon class for all cells
            override fun getColumnClass(column: Int): Class<*> {
                return Icon::class.java
            }
        }

        fun createPluginVersionPanel(): JPanel {
            val pluginPanel = JPanel()
            val pluginVersionLabel = JLabel()
            val pluginVer = PluginManagerCore.getPlugin(PluginId.getId("org.qatest.plugin.demo"))?.version
            pluginVersionLabel.text = "Plugin version: $pluginVer"
            pluginPanel.add(pluginVersionLabel)
            return pluginPanel
        }
        fun createGutterPanel(): JPanel {
            val pluginPanel = JPanel()
            pluginPanel.add(createGutterList())
            return pluginPanel
        }

        fun createGutterList(): JBTable {
            val fileName = getActiveFile()
            val listOfGutterIcons = arrayListOf<Icon>()

            if (fileName != null) {
                val editor = (FileEditorManager.getInstance(project) as FileEditorManagerImpl).getSelectedTextEditor()
                val gutter = editor?.gutter as EditorGutterComponentEx
                for (i in 0..(editor as EditorImpl).visibleLineCount) {
                    if (gutter.getGutterRenderers(i).isNotEmpty()) {
                        val cGutters = gutter.getGutterRenderers(i)
                        cGutters.forEach { cGutter ->
                            listOfGutterIcons.add(cGutter.icon)
                        }
                    }
                }
            }

            if (listOfGutterIcons.isEmpty()) {
                val model = DefaultTableModel(1, 1)
                if (fileName == null) {
                    model.setValueAt("There are no opened files", 0, 0)
                } else {
                    model.setValueAt("File: $fileName don't have Gutters", 0, 0)
                }
                val gutterTable = JBTable(model)
                gutterTable.setRowHeight(0,25)
                gutterTable.autoResizeMode = JBTable.AUTO_RESIZE_OFF
                return gutterTable
            }
            val model = gutterModelTable(1, listOfGutterIcons.size)
            listOfGutterIcons.forEachIndexed { idx, icon ->
                model.setValueAt(icon, 0, idx) //todo update to have 3-4 icons on Row or row width = 25-30
            }
            val gutterTable = JBTable(model)
            gutterTable.setRowHeight(0,40)
            gutterTable.autoResizeMode = JBTable.AUTO_RESIZE_OFF
            return gutterTable
        }

        fun createControlsPanel(toolWindow: ToolWindow): JPanel {
            val controlsPanel = JPanel()
            val refreshButton = JButton("Refresh")
            refreshButton.addActionListener { updateUiInfoMainPanel(toolWindow) }
            controlsPanel.add(refreshButton)
            val hideToolWindowButton = JButton("Hide")
            hideToolWindowButton.addActionListener { toolWindow.hide(null) }
            controlsPanel.add(hideToolWindowButton)
            return controlsPanel
        }

        fun updateUiInfoMainPanel(toolWindow: ToolWindow) {
            contentPanel.removeAll()
            contentPanel.add(createPluginVersionPanel(), BorderLayout.PAGE_START)
            contentPanel.add(createGutterPanel(), BorderLayout.CENTER)
            contentPanel.add(createControlsPanel(toolWindow), BorderLayout.CENTER)
            contentPanel.validate()
            contentPanel.repaint()
        }
    }
}
