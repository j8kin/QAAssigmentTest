package org.qatest.plugin.demo

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ex.EditorGutterComponentEx
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.WindowManager
import com.intellij.openapi.wm.impl.IdeFrameImpl
import com.intellij.ui.table.JBTable
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.table.DefaultTableModel

class PluginCreateViewPanel {
    fun createView(project: Project): JPanel {
        val panel = JPanel()
        panel.add(pluginVersionView(getPluginVersion()))
        panel.add(createGutterView(project))
        panel.add(pluginActiveUi(null))
        return panel
    }

    private fun getActiveFile(project: Project): VirtualFile? {
        return (FileEditorManager.getInstance(project) as FileEditorManagerImpl).currentFile
    }

    private fun getEditor(project: Project): Editor? {
        return (FileEditorManager.getInstance(project) as FileEditorManagerImpl).getSelectedTextEditor()
    }

    private fun getPluginVersion(): String {
        val version = PluginManagerCore.getPlugin(PluginId.getId("org.qatest.plugin.demo"))?.version
        if (version != null) {
            return version
        }
        return "No plugin version"
    }

    private fun getGutterIcons(project: Project): List<Icon> {
        val listOfGutterIcons = arrayListOf<Icon>()

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
        return listOfGutterIcons
    }

    private fun createGutterView(project: Project): JComponent {
        val fileName = getActiveFile(project)
        if (fileName == null) {
            val label = JLabel()
            label.text = "There are no opened project files"
            return label
        }
        val listOfGutterIcons = getGutterIcons(project)
        if (listOfGutterIcons.isEmpty()) {
            val label = JLabel()
            label.text = "File: $fileName has no gutters"
            return label
        }
        return pluginGutterIcons(listOfGutterIcons)
    }

    private fun pluginVersionView(pluginVersion: String): JLabel {
        val label = JLabel()
        label.text = "Plugin version: $pluginVersion"
        return label
    }

    private fun gutterModelTable(row: Int, col: Int): DefaultTableModel = object : DefaultTableModel(row, col) {
        //  Returning the Icon class for all cells
        override fun getColumnClass(column: Int): Class<*> {
            return Icon::class.java
        }
    }

    private fun pluginGutterIcons(gutterIcons: List<Icon>): JBTable {
        val model = gutterModelTable(1, gutterIcons.size)
        gutterIcons.forEachIndexed { idx, icon ->
            model.setValueAt(icon, 0, idx) //todo update to have 3-4 icons on Row or row width = 25-30
        }
        val gutterTable = JBTable(model)
        gutterTable.setRowHeight(0, 40)
        gutterTable.autoResizeMode = JBTable.AUTO_RESIZE_OFF
        return gutterTable
    }

    private fun getActiveUI(project: Project): JComponent? {
        //todo find active component
        val getUIInfoText = (WindowManager.getInstance().getFocusedComponent(project) as IdeFrameImpl).title
        //val text: String = if (getUIInfoText == null) "Dummy" else getUIInfoText.name

//            DaemonCodeAnalyzerImpl.getHighlights(getDocument(), HighlightSeverity.INFORMATION, project)
//            DaemonCodeAnalyzerImpl.getInstance(project)
        return null
    }

    private fun pluginActiveUi(activeUi: JComponent?): JLabel {
        val label = JLabel()
        if (activeUi != null) {
            label.text = "Active UI component: ${activeUi.name}"
        } else {
            label.text = "No Active UI Component"
        }
        return label
    }
}