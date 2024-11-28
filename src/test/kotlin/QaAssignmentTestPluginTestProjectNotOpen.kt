package org.qatest.plugin.demo

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.fixtures.ActionButtonFixture
import com.intellij.remoterobot.search.locators.byXpath
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
import java.time.Duration.ofSeconds


@ExtendWith(RemoteRobotExtension::class)
class QaAssignmentTestPluginTestProjectNotOpen {

    @BeforeEach
    fun waitForIde(remoteRobot: RemoteRobot) = with(remoteRobot) {
        CommonSteps(remoteRobot).closeProject()
        waitForIgnoringError(ofMinutes(3)) { remoteRobot.callJs("true") }
    }

    /**
     * Plugin Message Dialog closed after each test to make sure that
     *   even if test is failed then dialog will be closed
     */
    @AfterEach
    fun closeDialog(remoteRobot: RemoteRobot) = with(remoteRobot) {
        try {
            remoteRobot.find<ActionButtonFixture>(
                byXpath("//div[@text='OK']"),
                ofSeconds(5)
            ).click()
        } catch (ignore: Exception) {
            // if dialog is already closed or not opened then "OK" button not found
        }
    }

    @Test
    fun testPluginAction1WhenNoProjectOpened(remoteRobot: RemoteRobot) = with(remoteRobot) {
        welcomeFrame {
            // open dialog with plugin version
            step("Open dialog: QaAssignmentTestAction1") {
                keyboard { hotKey(VK_CONTROL, VK_ALT, VK_DIVIDE) }
                dialog("Action1 (Display Version)") {
                    val messageText = findText { (text) -> text.contains("Plugin version:") }
                    Assertions.assertNotNull(messageText)
                    Assertions.assertEquals("Plugin version: 0.1.0-SNAPSHOT", messageText.text)
                }
            }
        }
    }

    @Test
    fun testPluginAction2WhenNoProjectOpened(remoteRobot: RemoteRobot) = with(remoteRobot) {
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

    @Test
    fun testPluginAction3NotDisplayedWhenNoProjectOpened(remoteRobot: RemoteRobot) = with(remoteRobot) {
        welcomeFrame {
            // trying to open dialog with current Selected UI in opened project
            // dialog should not be displayed
            step("Trying to pen dialog: QaAssignmentTestAction3") {
                keyboard { hotKey(VK_CONTROL, VK_ALT, VK_BACK_SLASH) }
                val exception = assertThrows<WaitForConditionTimeoutException> {
                    dialog("Action3 (Display Active Component UI Info)") {
                        // should never be reached
                        Assertions.assertNull(
                            find<ActionButtonFixture>(
                                byXpath("//div[@text='OK']"),
                                ofSeconds(20)
                            )
                        )
                    }
                }
                Assertions.assertNotNull(exception.message)
            }
        }
    }
}