package com.je.playground.feature.tasks.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.entity.Task
import com.je.playground.designsystem.component.SnackbarComponent
import com.je.playground.designsystem.component.taskeditor.EditorModeButtonRow
import com.je.playground.designsystem.component.taskeditor.EditorTopBar
import com.je.playground.designsystem.component.taskeditor.MainTaskEditorComponent
import com.je.playground.designsystem.component.taskeditor.SingleTaskEditor
import com.je.playground.designsystem.component.taskeditor.SubTasksComponent


@Composable
fun TaskEditorScreen(
    viewModel: TaskEditorViewModel,
    onEditTaskClick: (Long) -> Unit,
    onEditSubTaskClick:  (Long, Int) -> Unit,
    onBackClick: () -> Unit
) {
    val taskEditorState: TaskEditorViewModel.State by viewModel.taskEditorUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarFlow.collect { event ->
            event.consume()?.let { message ->
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    TaskEditorScreen(
        taskEditorState = taskEditorState,
        snackbarHostState = snackbarHostState,
        onEditTaskClick = onEditTaskClick,
        onEditSubTaskClick = onEditSubTaskClick,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
fun TaskEditorScreen(
    taskEditorState: TaskEditorViewModel.State,
    snackbarHostState: SnackbarHostState,
    onEditTaskClick: (Long) -> Unit,
    onEditSubTaskClick:  (Long, Int) -> Unit,
    onEvent: (TaskEditorEvent) -> Unit,
    onBackClick: () -> Unit
) {
    when (taskEditorState) {
        is TaskEditorViewModel.State.Loading -> {}
        is TaskEditorViewModel.State.Ready -> {
            TaskEditorContent(
                task = taskEditorState.task,
                subTasks = taskEditorState.subTasks,
                isGroup = taskEditorState.isGroup,
                navigateToMainTaskEditorScreen = onEditTaskClick,
                navigateToSubTaskEditorScreen = onEditSubTaskClick,
                snackbarHostState = snackbarHostState,
                onEvent = onEvent,
                onBackPress = onBackClick
            )
        }

        TaskEditorViewModel.State.Saved -> onBackClick()
    }
}


@Composable
fun TaskEditorContent(
    task: Task,
    subTasks: List<SubTask>,
    isGroup: Boolean,
    navigateToMainTaskEditorScreen: (Long) -> Unit,
    navigateToSubTaskEditorScreen:  (Long, Int) -> Unit,
    snackbarHostState: SnackbarHostState,
    onEvent: (TaskEditorEvent) -> Unit,
    onBackPress: () -> Unit
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
    task: Task,
    subTasks: List<SubTask>,
    navigateToMainTaskEditorScreen: (Long) -> Unit,
    navigateToSubTaskEditorScreen: (Long, Int) -> Unit,
    onEvent: (TaskEditorEvent) -> Unit
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