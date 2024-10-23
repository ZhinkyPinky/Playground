package com.je.playground.feature.tasks.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.entity.Task
import com.je.playground.designsystem.component.SnackbarComponent
import com.je.playground.designsystem.component.taskeditor.EditorModeButtonRow
import com.je.playground.designsystem.component.taskeditor.EditorTopBar
import com.je.playground.designsystem.component.taskeditor.MainTaskEditorComponent
import com.je.playground.designsystem.component.taskeditor.SingleTaskEditor
import com.je.playground.designsystem.component.taskeditor.SubTasksComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun TaskEditorScreen(
    taskEditorViewModel : TaskEditorViewModel,
    navigateToMainTaskEditorScreen : () -> Unit,
    navigateToSubTaskEditorScreen : (Int) -> Unit,
    onBackPress : () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val localCoroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        taskEditorViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is TaskEditorViewModel.Event.ShowSnackbar -> {
                    localCoroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message
                        )
                    }
                }

                is TaskEditorViewModel.Event.Saved -> {
                    onBackPress()
                }
            }
        }
    }

    when (taskEditorViewModel.taskEditorUiState.collectAsState().value) {
        is TaskEditorViewModel.State.Loading -> {
            //TODO: Add loading screen?
            CircularProgressIndicator()
        }

        is TaskEditorViewModel.State.Ready -> {
            TaskEditorScreen(
                task = taskEditorViewModel.task.collectAsState().value,
                subTasks = taskEditorViewModel.subTasks,
                isGroup = taskEditorViewModel.isGroup.collectAsState().value,
                navigateToMainTaskEditorScreen = navigateToMainTaskEditorScreen,
                navigateToSubTaskEditorScreen = navigateToSubTaskEditorScreen,
                snackbarHostState = snackbarHostState,
                onEvent = taskEditorViewModel::onEvent,
                onBackPress = onBackPress
            )
        }
    }
}

@Composable
fun TaskEditorScreen(
    task : Task,
    subTasks : List<SubTask>,
    isGroup : Boolean,
    navigateToMainTaskEditorScreen : () -> Unit,
    navigateToSubTaskEditorScreen : (Int) -> Unit,
    snackbarHostState : SnackbarHostState,
    onEvent : (TaskEditorEvent) -> Unit,
    onBackPress : () -> Unit,
) {
    //var isGroup by rememberSaveable { mutableStateOf(subTasks.isNotEmpty()) }

    TaskEditorContent(
        task = task,
        subTasks = subTasks,
        isGroup = isGroup,
        navigateToMainTaskEditorScreen = navigateToMainTaskEditorScreen,
        navigateToSubTaskEditorScreen = navigateToSubTaskEditorScreen,
        snackbarHostState = snackbarHostState,
        onEvent = onEvent,
        onBackPress = onBackPress,
    )
}


@Composable
fun TaskEditorContent(
    task : Task,
    subTasks : List<SubTask>,
    isGroup : Boolean,
    navigateToMainTaskEditorScreen : () -> Unit,
    navigateToSubTaskEditorScreen : (Int) -> Unit,
    snackbarHostState : SnackbarHostState,
    onEvent : (TaskEditorEvent) -> Unit,
    onBackPress : () -> Unit
) {
    Scaffold(
        topBar = {
            EditorTopBar(
                text = "Edit",
                onEvent = onEvent,
                onBackPress = onBackPress
            )
        },
        snackbarHost = {
            SnackbarComponent(snackbarHostState = snackbarHostState)
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
                onClick = { onEvent(TaskEditorEvent.ToggleGroup) }
            )

            if (isGroup) {
                GroupTaskEditor(
                    task = task,
                    subTasks = subTasks,
                    navigateToMainTaskEditorScreen = navigateToMainTaskEditorScreen,
                    navigateToSubTaskEditorScreen = navigateToSubTaskEditorScreen,
                    onEvent = onEvent
                )
            } else {
                SingleTaskEditor(
                    task = task,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
fun GroupTaskEditor(
    task : Task,
    subTasks : List<SubTask>,
    navigateToMainTaskEditorScreen : () -> Unit,
    navigateToSubTaskEditorScreen : (Int) -> Unit,
    onEvent : (TaskEditorEvent) -> Unit
) {
    MainTaskEditorComponent(
        task = task,
        navigateToMainTaskEditorScreen
    )

    SubTasksComponent(
        subTasks = subTasks,
        onEvent = onEvent,
        navigateToSubTaskEditorScreen = navigateToSubTaskEditorScreen
    )
}