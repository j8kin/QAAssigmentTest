# QA Automation IDE Services test assignment
## Objective
Create a custom plugin for IntelliJ IDEA version 2024.3 using Kotlin
## Tasks
1. Implement the following three actions, ensuring that they are accessible from the `Tools` dropdown menu.
   1. Action 1: Display the installed version of the Kotlin plugin.
   2. Action 2: Display all Gutter Icons present in the currently opened file.
   3. Action 3: Display information about the currently selected UI Component.
2. Provide a concise checklist or test scenario to validate your plugin's functionality.
## Additional Suggestion
Due to PC slowness hotkeys are added to each plugin Action. UI Tests mostly use hotkeys to open related Plugin Action Dialogs. 
Three UI tests are using Tool Menu to open Action Dialog to make sure that plugin dialogs are accessible from Tool Menu.

All three dialogs were added into Tool Menu as the latest menu as part of SubMenu "QaAssignmentTest":
![Tools->QaAssignmentTest->Action(1,2,3)](docs/toolmenu.png)

## Launching
Due to PC slowness I'm not able to run & test that is why I suggest firs start local IDE server with:

`./gradlew clean runIdeForUiTests &`

from project root folder and after IDE Welcome Screen appear in separate cmd run from project root folder:

`./gradlew test`

and one more time activate Intellij IDE Welcome Screen

# Test Scenarios
## Action 1 (Display Kotlin plugin version)
1. Kotlin plugin installed
   1. On IDE Welcome Screen (only hotkeys could be used)
   2. When Project open (both hotkeys and Tool->QaAssignmentTest menu)
2. Kotlin plugin NOT installed (**MANUAL verification** no automated UI test)
   1. On IDE Welcome Screen (only hotkey could be used)
   2. When Project open (both hotkeys and Tool->QaAssignmentTest menu)
## Action 2 (Display list of Gutters)
1. On Welcome Screen (only hotkey could be used)
2. When Project opened (both hotkey and Tool menu)
   1. Observe that if file contains Gutters they are all displayed in Dialog
   2. Add 2-3 Gutters on the same line (for example it could be "run", "breakpoint" and "bookmark" gutters) and observe that they are displayed in dialog. 
   3. Remove one of the gutters (for example the gutter in the middle) and observe that all other gutters are in dialog
   4. Open file without gutters and observe that dialog contain message like: "File: $FullPath/FileName$ has no gutters"
   5. Close all files and observe that dialog contains message: "There are no opened project files"
## Action3 (Display Active Component UI Info)
**Disclaimer:**

This action works only when any project is opened

This is not working with any Menu Items any popups etc

In real plugin testing it would be necessary to verify all possible type of UI Components.
Here I verify only some Ui Components in ProjectTreeView and in File Editor
1. Editor Ui Component Info (open any source code file)
   1. Set cursor on any function name and observe that dialog contains info like: "PsiMethod:$Method Name$"
   2. Set cursor on any variable name and observe that dialog contains info like: "PsiLocalVariable:$Variable Name$"
   3. Set cursor on Non-Ui Component line (for example on "}") and observe that dialog has information: "No selected UI Component"
2. Project Tree View 
   1. Select any folder in project tree view and observe that dialog contains info like: "PsiDirectory:$FullPath$"
   2. Select class file in project tree view and observe that dialog contains: "PsiClass:$ClassName$"