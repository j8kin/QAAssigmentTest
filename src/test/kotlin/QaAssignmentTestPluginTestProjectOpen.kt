package org.qatest.plugin.demo

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.fixtures.ActionButtonFixture
import com.intellij.remoterobot.fixtures.JLabelFixture
import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.steps.CommonSteps
import com.intellij.remoterobot.stepsProcessing.step
import com.intellij.remoterobot.utils.keyboard
import com.intellij.remoterobot.utils.waitFor
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.qatest.plugin.demo.utils.*
import java.awt.event.KeyEvent.*
import java.time.Duration.ofMinutes
import java.time.Duration.ofSeconds

@ExtendWith(RemoteRobotExtension::class)
class QaAssignmentTestPluginTestProjectOpen {
    companion object {
        private lateinit var remoteRobot: RemoteRobot

        @JvmStatic
        @BeforeAll
        fun startUp() {
            remoteRobot = RemoteRobot("http://localhost:8082")
            createCommandLineApp()
        }

        /**
         * Close project only once after all "project" related tests complete
         *  since create new project is quite timing operation
         */
        @JvmStatic
        @AfterAll
        fun closeProject() {
            CommonSteps(remoteRobot).closeProject()
        }

        private fun createCommandLineApp() = with(remoteRobot) {
//            val sharedSteps = UiTestSharedSteps(this)

            welcomeFrame {
                createNewProjectLink.click()
                dialog("New Project") {
                    findText("Java").click()
                    checkBox("Add sample code").select()
                    button("Create").click()
                }
            }
            idea {
                // wait till project opened and indexed
                waitFor(ofMinutes(3)) { isDumbMode().not() }
                waitFor(ofSeconds(20)) {
                    button(byXpath("//div[@class='TrafficLightButton']")).hasText("Analyzing...").not()
                }

//                step("Create App file") {
//                    with(projectViewTree) {
//                        if (hasText("src").not()) {
//                            findText(projectName).doubleClick()
//                            waitFor { hasText("src") }
//                        }
//                        findText("src").click(MouseButton.RIGHT_BUTTON)
//                    }
//                    actionMenu("New").click()
//                    actionMenuItem("Java Class").click()
//                    keyboard { enterText("App"); enter() }
//                }
//            with(textEditor()) {
//                step("Write a code") {
//                    Thread.sleep(1_000)
//                    editor.findText("App").click()
//                    keyboard {
//                        key(VK_END)
//                        enter()
//                    }
//                    sharedSteps.autocomplete("main")
//                    sharedSteps.autocomplete("sout")
//                    keyboard { enterText("\""); enterText("Hello from UI test") }
//                }
//                step("Launch application") {
//                    waitFor(ofSeconds(20)) {
//                        button(byXpath("//div[@class='TrafficLightButton']")).hasText("Analyzing...").not()
//                    }
//                    menuBar.select("Build", "Build Project")
//                    waitFor { gutter.getIcons().isNotEmpty() }
//                    gutter.getIcons().first { it.description.contains("run.svg") }.click()
//                    this@idea.find<CommonContainerFixture>(
//                        byXpath("//div[@class='HeavyWeightWindow']"), ofSeconds(4)
//                    ).button(byXpath("//div[@disabledicon='execute.svg']"))
//                        .click()
//                }
//            }
//
//            val consoleLocator = byXpath("ConsoleViewImpl", "//div[@class='ConsoleViewImpl']")
//            step("Wait for Console appears") {
//                waitFor(ofMinutes(1)) { findAll<ContainerFixture>(consoleLocator).isNotEmpty() }
//            }
//            step("Check the message") {
//                waitFor(ofMinutes(1)) { find<ContainerFixture>(consoleLocator).hasText("Hello from UI test") }
//            }
            }
        }
    }

    @BeforeEach
    fun checkProjectIsOpen() = with(remoteRobot) {
        idea {
            Assertions.assertNotNull(projectName)
        }
    }

    /**
     * Plugin Message Dialog closed after each test to make sure that
     *   even if test is failed then dialog will be closed
     */
    @AfterEach
    fun closeDialog() = try {
        remoteRobot.find<ActionButtonFixture>(
            byXpath("//div[@text='OK']"),
            ofSeconds(5)
        ).click()
    } catch (ignore: Exception) {
        // if dialog is already closed or not opened then "OK" button not found
    }

    @Test
    fun testPluginAction1() = with(remoteRobot) {
        idea {
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
    fun testPluginAction1ToolMenu() = with(remoteRobot) {
        idea {
            step("Open dialog: QaAssignmentTestAction1 via Tool Menu") {
                // verify that dialog is available from Tool-> QaAssignmentTest -> Action1 (Display Version)
                menuBar.select("Tools", "QaAssignmentTest", "Action1 (Display Version)")
                dialog("Action1 (Display Version)") {
                    val messageText = findText { (text) -> text.contains("Plugin version:") }
                    Assertions.assertNotNull(messageText)
                    Assertions.assertEquals("Plugin version: 0.1.0-SNAPSHOT", messageText.text)
                }
            }
        }
    }

    /**
     * precondition: IntelliJ create Java project with Main.java
     *                 which contains 3 Gutters: 2 "Start execution" and 1 breakpoint
     */
    @Test
    fun testPluginAction2() = with(remoteRobot) {
        idea {
            step("Select main.java") {

            }
            step("Open dialog: QaAssignmentTestAction1") {
                keyboard { hotKey(VK_CONTROL, VK_ALT, VK_MULTIPLY) }
                dialog("Action2 (Display Gutters)") {
                    val listOfIcons = findAll<JLabelFixture>(byXpath("//div[@name='OptionPane.body']//div[@class='JPanel']"))
                    Assertions.assertNotNull(listOfIcons)
                }
            }
        }
    }
}