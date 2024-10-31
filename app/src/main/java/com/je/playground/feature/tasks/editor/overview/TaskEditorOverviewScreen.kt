package com.je.playground.feature.tasks.editor.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.entity.Task
import com.je.playground.designsystem.component.SnackbarComponent
import com.je.playground.designsystem.component.task.taskeditor.SubTasksComponent
import com.je.playground.designsystem.component.task.taskeditor.TaskEditorComponent
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews
import java.time.LocalDate
import java.time.LocalTime


@Composable
fun TaskEditorOverviewScreen(
    viewModel: TaskEditorOverviewViewModel,
    navigateToTaskEditor: (Long) -> Unit,
    navigateToSubTaskEditor: (Long, Long) -> Unit,
    onBackClick: () -> Unit
) {
    val taskEditorState: TaskEditorOverviewViewModel.State by viewModel.taskEditorOverviewState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            event.consume()?.let { message -> snackbarHostState.showSnackbar(message) }
        }
    }

    TaskEditorOverviewScreen(
        taskEditorState = taskEditorState,
        snackbarHostState = snackbarHostState,
        navigateToTaskEditor = navigateToTaskEditor,
        navigateToSubTaskEditor = navigateToSubTaskEditor,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
fun TaskEditorOverviewScreen(
    taskEditorState: TaskEditorOverviewViewModel.State,
    snackbarHostState: SnackbarHostState,
    navigateToTaskEditor: (Long) -> Unit,
    navigateToSubTaskEditor: (Long, Long) -> Unit,
    onEvent: (TaskEditorOverviewEvent) -> Unit,
    onBackClick: () -> Unit
) {
    when (taskEditorState) {
        is TaskEditorOverviewViewModel.State.Loading -> {}
        is TaskEditorOverviewViewModel.State.Ready -> {
            TaskEditorOverviewContent(
                task = taskEditorState.task,
                subTasks = taskEditorState.subTasks,
                navigateToTaskEditor = navigateToTaskEditor,
                navigateToSubTaskEditor = navigateToSubTaskEditor,
                snackbarHostState = snackbarHostState,
                onEvent = onEvent,
                onBackClick = onBackClick
            )
        }

        TaskEditorOverviewViewModel.State.Saved -> onBackClick()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorOverviewContent(
    task: Task,
    subTasks: List<SubTask>,
    navigateToTaskEditor: (Long) -> Unit,
    navigateToSubTaskEditor: (Long, Long) -> Unit,
    snackbarHostState: SnackbarHostState,
    onEvent: (TaskEditorOverviewEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(text = "Task") },
                    actions = {
                        IconButton(onClick = { navigateToTaskEditor(task.taskId) }) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit task",
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go Back",
                            )
                        }
                    },
                )
                HorizontalDivider()
            }
        },
        snackbarHost = { SnackbarComponent(snackbarHostState = snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(paddingValues)
        ) {
            TaskEditorComponent(
                task = task,
                navigateToTaskEditor
            )

            HorizontalDivider()

            SubTasksComponent(
                taskId = task.taskId,
                subTasks = subTasks,
                onEvent = onEvent,
                navigateToSubTaskEditor = navigateToSubTaskEditor
            )

            HorizontalDivider()
        }
    }
}

@ThemePreviews
@Composable
fun TaskEditorScreenPreview() {
    PlaygroundTheme {
        TaskEditorOverviewContent(
            task = Task(
                title = "Do thang",
                note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam blandit porta fringilla. Proin eu odio eget dolor placerat facilisis. Mauris aliquam purus vitae dolor fringilla congue. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.",
                startDate = LocalDate.now().plusDays(50),
                endDate = LocalDate.now().plusDays(58),
                startTime = LocalTime.now(),
                endTime = LocalTime.now().plusHours(1)
            ),
            subTasks = listOf(
                SubTask(
                    taskId = 0,
                    title = "Stuff",
                ),
                SubTask(
                    taskId = 0,
                    title = "Do stuff",
                    startDate = LocalDate.now().plusDays(50),
                    endDate = LocalDate.now().plusDays(58),
                    startTime = LocalTime.now(),
                    endTime = LocalTime.now().plusHours(1)
                ),
                SubTask(
                    taskId = 0,
                    title = "Do thing",
                    note = "Lorem ipsum dolor sit amet.",
                    startDate = LocalDate.now().plusDays(50),
                    endDate = LocalDate.now().plusDays(58),
                    startTime = LocalTime.now(),
                    endTime = LocalTime.now().plusHours(1)
                ),
                SubTask(
                    taskId = 0,
                    title = "Do other thing",
                    note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam blandit porta fringilla. Proin eu odio eget dolor placerat facilisis. Mauris aliquam purus vitae dolor fringilla congue. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.",
                    startDate = LocalDate.now().plusDays(50),
                    endDate = LocalDate.now().plusDays(58),
                    startTime = LocalTime.now(),
                    endTime = LocalTime.now().plusHours(1)
                ),
            ),
            navigateToTaskEditor = {},
            navigateToSubTaskEditor = { _, _ -> },
            snackbarHostState = SnackbarHostState(),
            onEvent = {},
            onBackClick = {},
        )
    }
}