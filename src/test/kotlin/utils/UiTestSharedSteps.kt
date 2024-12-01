package org.qatest.plugin.demo.utils

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.fixtures.ActionButtonFixture
import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.stepsProcessing.step
import com.intellij.remoterobot.utils.keyboard
import java.awt.event.KeyEvent.*
import java.time.Duration.ofSeconds

class UiTestSharedSteps(private val remoteRobot: RemoteRobot) {

    fun createNewCommandLineProject() {
        step("Create New Project", Runnable {
            val welcomeFrame: WelcomeFrameFixture =
                remoteRobot.find(WelcomeFrameFixture::class.java, ofSeconds(10))
            welcomeFrame.createNewProjectLink().click()

            val newProjectDialog: DialogFixture = welcomeFrame.find(
                DialogFixture::class.java,
                DialogFixture.byTitle("New Project"),
                ofSeconds(20)
            )
            newProjectDialog.findText("Java").click()
            newProjectDialog.button("Create").click()
        })
    }

    fun goToLineAndColumn(row: Int, column: Int) = with(remoteRobot) {
        idea {
            if (remoteRobot.isMac()) keyboard { hotKey(VK_META, VK_L) }
            else keyboard { hotKey(VK_CONTROL, VK_G) }

            keyboard { enterText("$row:$column") }
            closeDialog()
        }
    }

    fun closeDialog() = try {
        remoteRobot.find<ActionButtonFixture>(
            byXpath("//div[@text='OK']"),
            ofSeconds(5)
        ).click()
    } catch (ignore: Exception) {
        // if dialog is already closed or not opened then "OK" button not found
    }

    fun openAction1(viaToolMenu: Boolean = false) = with(remoteRobot) {
        idea {
            if (remoteRobot.isMac() || viaToolMenu) {
                menuBar.select("Tools", "QaAssignmentTest", "Action1 (Display Version)")
            } else keyboard { hotKey(VK_CONTROL, VK_ALT, VK_DIVIDE) }
        }
    }

    fun openAction2(viaToolMenu: Boolean = false) = with(remoteRobot) {
        idea {
            if (remoteRobot.isMac() || viaToolMenu) {
                menuBar.select("Tools", "QaAssignmentTest", "Action2 (Display Gutters)")
            } else keyboard { hotKey(VK_CONTROL, VK_ALT, VK_MULTIPLY) }
        }
    }

    fun openAction3(viaToolMenu: Boolean = false) = with(remoteRobot) {
        idea {
            if (remoteRobot.isMac() || viaToolMenu) {
                menuBar.select("Tools", "QaAssignmentTest", "Action3 (Display Active Component UI Info)")
            } else keyboard { hotKey(VK_CONTROL, VK_ALT, VK_BACK_SLASH) }
        }
    }

    fun toggleBreakpoint() = with(remoteRobot) {
        idea {
            if (isMac()) keyboard { hotKey(VK_META, VK_F8) }
            else keyboard { hotKey(VK_CONTROL, VK_F8) }
        }
    }

    fun toggleBookmark() = with(remoteRobot) {
        idea {
            if (isMac()) keyboard { hotKey(VK_F3) }
            else keyboard { hotKey(VK_F11) }
        }
    }

    fun closeEditorActiveTab() = with(remoteRobot) {
        idea {
            if (isMac()) keyboard { hotKey(VK_META, VK_W) }
            else keyboard { hotKey(VK_CONTROL, VK_F4) }
        }
    }
}
