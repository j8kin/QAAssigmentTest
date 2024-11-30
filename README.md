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
