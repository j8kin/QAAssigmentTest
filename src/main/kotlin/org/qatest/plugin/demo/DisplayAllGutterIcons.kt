package org.qatest.plugin.demo

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.editor.ex.EditorGutterComponentEx
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.Messages
import com.intellij.ui.table.JBTable
import javax.swing.Icon
import javax.swing.JTable
import javax.swing.table.DefaultTableModel

class DisplayAllGutterIcons : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val openedFile = AnActionEvent.getInjectedDataContext(e.dataContext).getData(PlatformDataKeys.VIRTUAL_FILE)
        val editor = AnActionEvent.getInjectedDataContext(e.dataContext).getData(PlatformDataKeys.EDITOR)
        val gutter = editor?.gutter as EditorGutterComponentEx
        val listOfGutterIcons = arrayListOf<Icon>()

        val fileName = openedFile?.name
        for (i in 0..(editor as EditorImpl).visibleLineCount) {
            if (gutter.getGutterRenderers(i).isNotEmpty()) {
                val cGutters = gutter.getGutterRenderers(i)
                cGutters.forEach { cGutter ->
                    listOfGutterIcons.add(cGutter.icon)
                }
            }
        }

        if (listOfGutterIcons.isEmpty()) {
            Messages.showMessageDialog(
                "FilePath: $fileName has no Gutters",
                "QATestPlugin Display All Gutters Icons",
                Messages.getInformationIcon()
            )
        } else {
            val dialog = DialogBuilder()
            val model: DefaultTableModel = object : DefaultTableModel(1, listOfGutterIcons.size) {
                //  Returning the Icon class for all cells
                override fun getColumnClass(column: Int): Class<*> {
                    return Icon::class.java
                }
            }

            listOfGutterIcons.forEachIndexed { idx, icon ->
                model.setValueAt(icon, 0, idx)
            }

            val table = JBTable(model)
            table.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS

            dialog.setCenterPanel(table)
            dialog.show()
        }
    }
}