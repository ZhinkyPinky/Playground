package com.je.playground.view.taskview.taskeditor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.je.playground.database.tasks.entity.MainTask
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.view.taskview.taskeditor.viewmodel.TaskEditorViewModel

@Composable
fun TaskEditorScreen(
    taskEditorViewModel : TaskEditorViewModel,
    navigateToMainTaskEditorScreen : () -> Unit,
    navigateToSubTaskEditorScreen : (Int) -> Unit,
    onBackPress : () -> Unit,
) {
    when (taskEditorViewModel.taskEditorUiState.collectAsState().value) {
        is TaskEditorViewModel.State.Loading -> {
            //TODO: Add loading screen?
            CircularProgressIndicator()
        }

        is TaskEditorViewModel.State.Ready -> {
            TaskEditorScreen(
                mainTask = taskEditorViewModel.mainTask.collectAsState().value,
                updateMainTask = taskEditorViewModel::updateMainTask,
                subTasks = taskEditorViewModel.subTasks,
                removeSubTask = taskEditorViewModel::removeSubTask,
                saveMainTaskWithSubTasks = {
                    taskEditorViewModel.saveMainTaskWithSubTasks()
                    onBackPress()
                },
                navigateToMainTaskEditorScreen = navigateToMainTaskEditorScreen,
                navigateToSubTaskEditorScreen = navigateToSubTaskEditorScreen,
                onBackPress = onBackPress
            )
        }
    }
}

@Composable
fun TaskEditorScreen(
    mainTask : MainTask,
    updateMainTask : (MainTask) -> Unit,
    subTasks : List<SubTask>,
    removeSubTask : (Int) -> Unit,
    saveMainTaskWithSubTasks : () -> Unit,
    navigateToMainTaskEditorScreen : () -> Unit,
    navigateToSubTaskEditorScreen : (Int) -> Unit,
    onBackPress : () -> Unit,
) {
    var isGroup by rememberSaveable { mutableStateOf(subTasks.isNotEmpty()) }

    TaskEditorContent(
        mainTask = mainTask,
        saveMainTaskWithSubTasks = saveMainTaskWithSubTasks,
        updateMainTask = updateMainTask,
        subTasks = subTasks,
        removeSubTask = removeSubTask,
        isGroup = isGroup,
        toggleIsGroup = {
            isGroup =
                if (subTasks.isNotEmpty()) {
                    true
                } else {
                    !isGroup
                }
        },
        navigateToMainTaskEditorScreen = navigateToMainTaskEditorScreen,
        navigateToSubTaskEditorScreen = navigateToSubTaskEditorScreen,
        onBackPress = onBackPress,
    )
}


@Composable
fun TaskEditorContent(
    mainTask : MainTask,
    updateMainTask : (MainTask) -> Unit,
    saveMainTaskWithSubTasks : () -> Unit,
    subTasks : List<SubTask>,
    removeSubTask : (Int) -> Unit,
    isGroup : Boolean,
    toggleIsGroup : () -> Unit,
    navigateToMainTaskEditorScreen : () -> Unit,
    navigateToSubTaskEditorScreen : (Int) -> Unit,
    onBackPress : () -> Unit
) {
    Scaffold(
        topBar = {
            EditorTopBar(
                text = "Edit",
                onSave = saveMainTaskWithSubTasks,
                onBackPress
            )
        },
        containerColor = if (isGroup) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            EditorModeButtonRow(
                isGroup = isGroup,
                onClick = toggleIsGroup
            )

            if (isGroup) {

                GroupTaskEditor(
                    mainTask = mainTask,
                    subTasks = subTasks,
                    removeSubTask = removeSubTask,
                    navigateToMainTaskEditorScreen = navigateToMainTaskEditorScreen,
                    navigateToSubTaskEditorScreen = navigateToSubTaskEditorScreen
                )
            } else {
                SingleTaskEditor(
                    mainTask = mainTask,
                    updateMainTask = updateMainTask
                )
            }
        }
    }
}

@Composable
fun GroupTaskEditor(
    mainTask : MainTask,
    subTasks : List<SubTask>,
    removeSubTask : (Int) -> Unit,
    navigateToMainTaskEditorScreen : () -> Unit,
    navigateToSubTaskEditorScreen : (Int) -> Unit,
) {
    MainTaskEditorComponent(
        mainTask = mainTask,
        navigateToMainTaskEditorScreen
    )

    SubTasksComponent(
        subTasks = subTasks,
        removeSubTask = removeSubTask,
        navigateToSubTaskEditorScreen = navigateToSubTaskEditorScreen
    )
}