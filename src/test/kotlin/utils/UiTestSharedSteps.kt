package org.qatest.plugin.demo.utils

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.fixtures.ComponentFixture
import com.intellij.remoterobot.fixtures.dataExtractor.contains
import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.stepsProcessing.step
import com.intellij.remoterobot.utils.Keyboard
import com.intellij.remoterobot.utils.hasSingleComponent
import com.intellij.remoterobot.utils.waitFor
import java.awt.event.KeyEvent
import java.time.Duration

class UiTestSharedSteps(private val remoteRobot: RemoteRobot) {
    private val keyboard = Keyboard(remoteRobot)

    fun createNewCommandLineProject() {
        step("Create New Project", Runnable {
            val welcomeFrame: WelcomeFrameFixture =
                remoteRobot.find(WelcomeFrameFixture::class.java, Duration.ofSeconds(10))
            welcomeFrame.createNewProjectLink().click()

            val newProjectDialog: DialogFixture = welcomeFrame.find(
                DialogFixture::class.java,
                DialogFixture.byTitle("New Project"),
                Duration.ofSeconds(20)
            )
            newProjectDialog.findText("Java").click()
            newProjectDialog.button("Create").click()
        })
    }

//    fun closeTipOfTheDay() {
//        step("Close Tip of the Day if it appears", Runnable {
//            waitFor(Duration.ofSeconds(20)) {
//                remoteRobot.findAll<T>(
//                    DialogFixture::class.java,
//                    byXpath("//div[@class='MyDialog'][.//div[@text='Running startup activities...']]")
//                ).size == 0
//            }
//            val idea: IdeaFrame = remoteRobot.find(IdeaFrame::class.java, Duration.ofSeconds(10))
//            idea.dumbAware {
//                try {
//                    idea.find(DialogFixture::class.java, byTitle("Tip of the Day")).button("Close").click()
//                } catch (ignore: Throwable) {
//                }
//                Unit
//            }
//        })
//    }

    fun autocomplete(text: String) {
        step("Autocomplete '$text'", Runnable {
            val completionMenu = byXpath("//div[@class='HeavyWeightWindow']//div[@class='LookupList']")
            val keyboard = Keyboard(remoteRobot)
            keyboard.enterText(text)
            waitFor(Duration.ofSeconds(10)) { remoteRobot.hasSingleComponent(completionMenu) }
            remoteRobot.find(ComponentFixture::class.java, completionMenu)
                .findText(contains(text))
                .click()
            keyboard.enter()
        })
    }

    fun goToLineAndColumn(row: Int, column: Int) {
        if (remoteRobot.isMac()) keyboard.hotKey(KeyEvent.VK_META, KeyEvent.VK_L)
        else keyboard.hotKey(KeyEvent.VK_CONTROL, KeyEvent.VK_G)
        keyboard.enterText("$row:$column")
        keyboard.enter()
    }
}
