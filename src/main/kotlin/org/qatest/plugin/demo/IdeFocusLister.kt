package org.qatest.plugin.demo

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ex.FocusChangeListener
import com.intellij.openapi.ui.Messages


class IdeFocusLister: FocusChangeListener {
    override fun focusGained(editor: Editor) {
        super.focusGained(editor)
            Messages.showMessageDialog(editor.component.name, "Focus Gained", Messages.getInformationIcon())
    }
}