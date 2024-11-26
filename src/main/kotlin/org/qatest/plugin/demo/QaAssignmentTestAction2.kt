package org.qatest.plugin.demo

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.ex.EditorGutterComponentEx
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.table.JBTable
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.table.DefaultTableModel

class QaAssignmentTestAction2: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        if (project != null) {
            val dialog = DialogBuilder()
            dialog.title(e.presentation.text)
            dialog.setCenterPanel(createGutterView(project))
            dialog.show()
        }
        else {
            Messages.showMessageDialog(
                "There are no active project",
                e.presentation.text,
                Messages.getInformationIcon())
        }
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

    private fun pluginGutterIcons(gutterIcons: List<Icon>): JBTable {
        val model = object : DefaultTableModel(1, gutterIcons.size) {
            //  Returning the Icon class for all cells
            override fun getColumnClass(column: Int): Class<*> {
                return Icon::class.java
            }
        }
        gutterIcons.forEachIndexed { idx, icon ->
            model.setValueAt(icon, 0, idx) //todo update to have 3-4 icons on Row or row width = 25-30
        }
        val gutterTable = JBTable(model)
        gutterTable.setRowHeight(0, 40)
        gutterTable.autoResizeMode = JBTable.AUTO_RESIZE_OFF
        return gutterTable
    }

    private fun createGutterView(project: Project): JComponent {
        val fileName = (FileEditorManager.getInstance(project) as FileEditorManagerImpl).currentFile
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
}