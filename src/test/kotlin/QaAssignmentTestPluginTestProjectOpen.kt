package org.qatest.plugin.demo

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.fixtures.GutterIcon
import com.intellij.remoterobot.fixtures.JLabelFixture
import com.intellij.remoterobot.fixtures.JTextFieldFixture
import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.steps.CommonSteps
import com.intellij.remoterobot.stepsProcessing.step
import com.intellij.remoterobot.utils.keyboard
import com.intellij.remoterobot.utils.waitFor
import org.assertj.swing.core.MouseButton
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
        private lateinit var uiTestHelper: UiTestSharedSteps

        @JvmStatic
        @BeforeAll
        fun startUp() {
            remoteRobot = RemoteRobot("http://localhost:8082")
            uiTestHelper = UiTestSharedSteps(remoteRobot)

            with(remoteRobot) {
                welcomeFrame {
                    uiTestHelper.createNewCommandLineProject()
                }
                idea {
                    // wait till project opened and indexed
                    waitFor(ofMinutes(3)) { isDumbMode().not() }
                    step("Select Main.java") {
                        with(projectViewTree) {
                            if (hasText("Main").not()) {
                                findText("src").doubleClick()
                                waitFor { hasText("src") }
                            }
                            findText("Main").doubleClick() // open Main.java if it is not open
                        }
                        with(textEditor()) {
                            waitFor(ofSeconds(20)) { gutter.getIcons().size >= 2 } // Main.java should have at least 2 Gutters
                        }
                    }
                }
            }
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
    }

    @BeforeEach
    fun checkProjectIsOpen() = with(remoteRobot) {
        idea {
            // wait till project opened and indexed
            waitFor(ofMinutes(3)) { isDumbMode().not() }
            Assertions.assertNotNull(projectName)
            step("Select Main.java") {
                with(projectViewTree) {
                    if (hasText("Main").not()) {
                        findText("src").doubleClick()
                        waitFor { hasText("src") }
                    }
                    findText("Main").doubleClick() // open Main.java if it is not open
                }
                with(textEditor()) {
                    waitFor(ofSeconds(20)) { gutter.getIcons().size >= 2 } // Main.java should have at least 2 Gutters
                }
            }
        }
    }

    /**
     * Plugin Message Dialog closed after each test to make sure that
     *   even if test is failed then dialog will be closed
     */
    @AfterEach
    fun closeDialog() = uiTestHelper.closeDialog()

    /***********************************************************************/
    /** QA Assignment Test: Action 1                                      **/
    /***********************************************************************/
    @Test
    fun testPluginAction1() = with(remoteRobot) {
        idea {
            step("Open dialog: QaAssignmentTestAction1") {
                uiTestHelper.openAction1()
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

    @Test
    fun testPluginAction1ToolMenu() = with(remoteRobot) {
        idea {
            step("Open dialog: QaAssignmentTestAction1 via Tool Menu") {
                // verify that dialog is available from Tool-> QaAssignmentTest -> Action1 (Display Version)
                //menuBar.select("Tools", "QaAssignmentTest", "Action1 (Display Version)")
                uiTestHelper.openAction1(true)
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

    /***********************************************************************/
    /** QA Assignment Test: Action 2                                      **/
    /***********************************************************************/
    @Test
    fun testPluginAction2ToolMenu() = with(remoteRobot) {
        idea {
            step("Get existing Gutters") {
                with(textEditor()) {
                    waitFor(ofSeconds(20)) { gutter.getIcons().size >= 2 } // Main.java should have at least 2 Gutters
                }
            }
            step("Open dialog: QaAssignmentTestAction2 via Tool Menu") {
                // verify that dialog is available from Tool-> QaAssignmentTest -> Action1 (Display Version)
                uiTestHelper.openAction2(true)
                dialog("Action2 (Display Gutters)") {
                    val listOfIcons =
                        findAll<JLabelFixture>(byXpath("//div[@name='OptionPane.body']//div[@class='JLabel']"))
                    Assertions.assertNotNull(listOfIcons)
                    Assertions.assertTrue(listOfIcons.isNotEmpty())
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
        var listOfGutters: List<GutterIcon>? = null
        idea {
            step("Get existing Gutters") {
                with(textEditor()) {
                    waitFor(ofSeconds(20)) { gutter.getIcons().size >= 2 } // Main.java should have at least 2 Gutters
                    listOfGutters = gutter.getIcons()
                }
            }
            step("Open dialog: QaAssignmentTestAction2") {
                uiTestHelper.openAction2()
                dialog("Action2 (Display Gutters)") {
                    val listOfIcons =
                        findAll<JLabelFixture>(byXpath("//div[@name='OptionPane.body']//div[@class='JLabel']"))
                    checkGutters(listOfGutters!!, listOfIcons)
                }
            }
        }
    }

    @Test
    fun testPluginAction2ThreeGutterInRow() = with(remoteRobot) {
        var listOfGutters: List<GutterIcon>? = null
        var listOfIcons: List<JLabelFixture>? = null
        idea {
            step("Get existing Gutters") {
                with(textEditor()) {
                    waitFor(ofSeconds(20)) { gutter.getIcons().size >= 2 } // Main.java should have at least 2 Gutters
                    Assertions.assertTrue(gutter.getIcons().size >= 2)
                }
            }
            step("Add breakpoint and bookmark on line with void main() function") {
                with(textEditor()) {
                    editor.findText("static").click()
                    keyboard { hotKey(VK_CONTROL, VK_F8) } // set breakpoint
                    keyboard { hotKey(VK_CONTROL, VK_SHIFT, VK_3) } // set bookmark (3)

                    listOfGutters = gutter.getIcons()
                }
            }
            step("Open dialog: QaAssignmentTestAction2") {
                uiTestHelper.openAction2()
                dialog("Action2 (Display Gutters)") {
                    listOfIcons =
                        findAll<JLabelFixture>(byXpath("//div[@name='OptionPane.body']//div[@class='JLabel']"))

                    // close dialog to be able to remove breakpoint and bookmark to avoid unexpected case dependency
                    uiTestHelper.closeDialog()
                }
            }
            step("Cleanup Additional Icons to avoid case dependency") {
                with(textEditor()) {
                    editor.findText("main").click()
                    keyboard { hotKey(VK_CONTROL, VK_F8) } // set breakpoint
                    keyboard { hotKey(VK_CONTROL, VK_SHIFT, VK_3) } // set bookmark (3)
                }
            }
            step("Verify list of Icons in QaAssignmentTestAction2") {
                checkGutters(listOfGutters!!, listOfIcons!!)
            }
        }
    }

    @Test
    fun testPluginAction2NoGutters() = with(remoteRobot) {
        idea {
            step("Create noGutter.txt file which contains no Gutters") {
                with(projectViewTree) {
                    if (hasText("src").not()) {
                        findText(projectName).doubleClick()
                        waitFor { hasText("src") }
                    }
                    findText("src").click(MouseButton.RIGHT_BUTTON)
                }
                actionMenu("New").click()
                actionMenuItem("File").click()
                keyboard { enterText("noGutter.txt"); enter() }
            }
            var messageText: String? = null
            step("Open dialog: QaAssignmentTestAction2") {
                uiTestHelper.openAction2()
                dialog("Action2 (Display Gutters)") {
                    messageText = findText { (text) -> text.contains("File: ") }.text

                    closeDialog()
                }
            }
            step("Close noGutter.txt to avoid case dependency") {
                keyboard { hotKey(VK_CONTROL, VK_F4) }
            }
            step("Verify Action2 dialog text") {
                Assertions.assertNotNull(messageText)
                Assertions.assertTrue(messageText!!.contains("noGutter.txt has no gutters"))
            }
        }
    }

    @Test
    fun testPluginAction2NoOpenedFiles() = with(remoteRobot) {
        step("Close Main.java") {
            keyboard { hotKey(VK_CONTROL, VK_F4) }
        }
        step("Open dialog: QaAssignmentTestAction2") {
            uiTestHelper.openAction2()
            idea {
                dialog("Action2 (Display Gutters)") {
                    val messageText = findText { (text) -> text.contains("There are no opened project files") }
                    Assertions.assertNotNull(messageText)
                    Assertions.assertEquals("There are no opened project files", messageText.text)
                }
            }
        }
    }

    private fun checkGutters(expected: List<GutterIcon>, actual: List<JLabelFixture>) {
        Assertions.assertNotNull(actual)

        Assertions.assertEquals(expected.size, actual.size)
//        Assertions.assertIterableEquals(listOfGutters,listOfIcons)
    }

    /***********************************************************************/
    /** QA Assignment Test: Action 3                                      **/
    /***********************************************************************/
    @Test
    fun testPluginAction3ToolMenu() = with(remoteRobot) {
        idea {
            step("Select main function in editor") {
                with(textEditor()) {
                    editor.findText("main").click()
                }
            }
            step("Open Action3 (Display Active Component UI Info) via Tool Menu") {
                // verify that dialog is available from Tool-> QaAssignmentTest -> Action3 (Display Active Component UI Info)
                uiTestHelper.openAction3(true)
                dialog("Action3 (Display Active Component UI Info)") {
                    val messageText =
                        find<JTextFieldFixture>(byXpath("//div[contains(@visible_text, 'Selected')]"), ofSeconds(2))
                    Assertions.assertNotNull(messageText)
                    Assertions.assertEquals(3, messageText.retrieveData().textDataList.size)
                    Assertions.assertEquals("Selected UI Component:", messageText.retrieveData().textDataList[0].text)
                    // second line contains empty line with invisible character that is why do not verifying it
                    Assertions.assertEquals("PsiMethod:main", messageText.retrieveData().textDataList[2].text)
                }
            }
        }
    }

    @Test
    fun testPluginAction3NoComponentSelected() = with(remoteRobot) {
        idea {
            step("Goto non ui-component line '{'") {
                with(textEditor()) {
                    editor.findText("{").click()
                }
            }
            step("Open Action3 (Display Active Component UI Info)") {
                uiTestHelper.openAction3()
                dialog("Action3 (Display Active Component UI Info)") {
                    val messageText = findText { (text) -> text.contains("No selected UI Component") }
                    Assertions.assertNotNull(messageText)
                    Assertions.assertEquals("No selected UI Component", messageText.text)
                }
            }
        }
    }

    @Test
    fun testPluginAction3SelectProjectTreeElement() = with(remoteRobot) {
        idea {
            val project = projectName
            step("Select 'src' folder in Project Tree view") {
                with(projectViewTree) {
                    if (hasText("src").not()) {
                        findText(projectName).doubleClick()
                        waitFor { hasText("src") }
                    }
                    findText("src").click(MouseButton.LEFT_BUTTON) // select 'src' folder
                }
            }
            step("Open Action3 (Display Active Component UI Info)") {
                uiTestHelper.openAction3()
                dialog("Action3 (Display Active Component UI Info)") {
                    val messageText =
                        find<JTextFieldFixture>(byXpath("//div[contains(@visible_text, 'Selected')]"), ofSeconds(2))
                    Assertions.assertNotNull(messageText)
                    Assertions.assertTrue(messageText.retrieveData().textDataList.size >= 3)
                    // folder path could be pretty long and that is why it could be split on 2 and moe lines like:
                    //
                    // PsiDirectory: /my/super/long/path/to/project/
                    // folder/in/project/tree/view
                    //
                    // that is why concatenate them in one line and then verify
                    var folderPathText = ""
                    for (i in 2..<messageText.retrieveData().textDataList.size) {
                        folderPathText = "$folderPathText${messageText.retrieveData().textDataList[i].text}"
                    }
                    Assertions.assertEquals("Selected UI Component:", messageText.retrieveData().textDataList[0].text)
                    Assertions.assertTrue(folderPathText.startsWith("PsiDirectory:"))
                    Assertions.assertTrue(folderPathText.endsWith("\\$project\\src"))
                }
            }
        }
    }

    @Test
    fun testPluginAction3SelectEditorUiComponent() = with(remoteRobot) {
        idea {
            step("Select ui component in editor") {
                uiTestHelper.goToLineAndColumn(10, 33)
            }
            step("Open Action3 (Display Active Component UI Info) via Tool Menu") {
                uiTestHelper.openAction3()
                dialog("Action3 (Display Active Component UI Info)") {
                    val messageText =
                        find<JTextFieldFixture>(byXpath("//div[contains(@visible_text, 'Selected')]"), ofSeconds(2))
                    Assertions.assertNotNull(messageText)
                    Assertions.assertEquals(3, messageText.retrieveData().textDataList.size)
                    Assertions.assertEquals("Selected UI Component:", messageText.retrieveData().textDataList[0].text)
                    // second line contains empty line with invisible character that is why do not verifying it
                    Assertions.assertEquals("PsiLocalVariable:i", messageText.retrieveData().textDataList[2].text)
                }
            }
        }
    }
}