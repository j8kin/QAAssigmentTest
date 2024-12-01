package org.qatest.plugin.demo

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.steps.CommonSteps
import com.intellij.remoterobot.stepsProcessing.step
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException
import com.intellij.remoterobot.utils.keyboard
import com.intellij.remoterobot.utils.waitForIgnoringError
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.qatest.plugin.demo.utils.*
import java.awt.event.KeyEvent.*
import java.time.Duration.ofMinutes


@ExtendWith(RemoteRobotExtension::class)
class QaAssignmentTestPluginTestProjectNotOpen {

    @BeforeEach
    fun waitForIde(remoteRobot: RemoteRobot) {
        CommonSteps(remoteRobot).closeProject()
        return waitForIgnoringError(ofMinutes(3)) { remoteRobot.callJs("true") }
    }

    /**
     * Plugin Message Dialog closed after each test to make sure that
     *   even if test is failed then dialog will be closed
     */
    @AfterEach
    fun closeDialog(remoteRobot: RemoteRobot) = UiTestSharedSteps(remoteRobot).closeDialog()

    @Test
    fun testPluginAction1WhenNoProjectOpened(remoteRobot: RemoteRobot) = with(remoteRobot) {
        // this test is using hotkey with CTRL and ALT not able to verify on MAC that is why
        // this test is disabled for MAC since welcome screen has no menu and not able to open
        // dialogs via menu
        if (!remoteRobot.isMac()) {
            welcomeFrame {
                // open dialog with plugin version
                step("Open dialog: QaAssignmentTestAction1") {
                    keyboard { hotKey(VK_CONTROL, VK_ALT, VK_DIVIDE) }
                    dialog("Action1 (Display Version)") {
                        val messageText = findText { (text) -> text.contains("Kotlin Plugin version:") }
                        Assertions.assertNotNull(messageText)
                        // actually the real plugin version could be verified as RegExp
                        // exact version verification is not good since it could be different
                        // from one pc to another
                        // as a quick solution just verify that it is not empty
                        Assertions.assertTrue(messageText.text.substring(23).isNotEmpty())
                    }
                }
            }
        }
    }

    @Test
    fun testPluginAction2WhenNoProjectOpened(remoteRobot: RemoteRobot) = with(remoteRobot) {
        // this test is using hotkey with CTRL and ALT not able to verify on MAC that is why
        // this test is disabled for MAC since welcome screen has no menu and not able to open
        // dialogs via menu
        if (!remoteRobot.isMac()) {
            welcomeFrame {
                // open dialog with list of gutter icons in current editor
                step("Open dialog: QaAssignmentTestAction2") {
                    keyboard { hotKey(VK_CONTROL, VK_ALT, VK_MULTIPLY) }
                    dialog("Action2 (Display Gutters)") {
                        Assertions.assertNotNull(findText("There are no active project"))
                    }
                }
            }
        }
    }

    @Test
    fun testPluginAction3NotDisplayedWhenNoProjectOpened(remoteRobot: RemoteRobot) = with(remoteRobot) {
        // this test is using hotkey with CTRL and ALT not able to verify on MAC that is why
        // this test is disabled for MAC since welcome screen has no menu and not able to open
        // dialogs via menu
        if (!remoteRobot.isMac()) {
            welcomeFrame {
                // trying to open dialog with current Selected UI in opened project
                // dialog should not be displayed
                step("Trying to pen dialog: QaAssignmentTestAction3") {
                    keyboard { hotKey(VK_CONTROL, VK_ALT, VK_BACK_SLASH) }
                    val exception = assertThrows<WaitForConditionTimeoutException> {
                        dialog("Action3 (Display Active Component UI Info)") {
                            // should never be reached
                            UiTestSharedSteps(remoteRobot).closeDialog()
                        }
                    }
                    Assertions.assertNotNull(exception.message)
                }
            }
        }
    }
}