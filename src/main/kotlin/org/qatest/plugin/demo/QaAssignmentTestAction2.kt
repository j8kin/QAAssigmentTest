package org.qatest.plugin.demo

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.ex.EditorGutterComponentEx
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import javax.swing.*
import javax.swing.JOptionPane.INFORMATION_MESSAGE

class QaAssignmentTestAction2 : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        if (project != null) {
            JOptionPane.showMessageDialog(
                null,
                createGutterView(project),
                e.presentation.text,
                INFORMATION_MESSAGE
            )
        } else {
            Messages.showMessageDialog(
                "There are no active project",
                e.presentation.text,
                Messages.getInformationIcon()
            )
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

        val gutterIconsPanel = JPanel()

        listOfGutterIcons.forEach { icon ->
            gutterIconsPanel.add(JLabel(icon))
        }
        return gutterIconsPanel
    }
}