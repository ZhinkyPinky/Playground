package com.je.playground.feature.tasks.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
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
import com.je.playground.designsystem.component.task.taskeditor.EditorTopBar
import com.je.playground.designsystem.component.task.taskeditor.TaskEditorComponent
import com.je.playground.designsystem.component.task.taskeditor.SubTasksComponent
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews
import java.time.LocalDate
import java.time.LocalTime


@Composable
fun TaskEditorOverviewScreen(
    viewModel: TaskEditorViewModel,
    navigateToTaskEditorScreen: (Long) -> Unit,
    navigateToSubTaskEditorScreen: (Int) -> Unit,
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

    TaskEditorOverviewScreen(
        taskEditorState = taskEditorState,
        snackbarHostState = snackbarHostState,
        navigateToTaskEditorScreen = navigateToTaskEditorScreen,
        navigateToSubTaskEditorScreen = navigateToSubTaskEditorScreen,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
fun TaskEditorOverviewScreen(
    taskEditorState: TaskEditorViewModel.State,
    snackbarHostState: SnackbarHostState,
    navigateToTaskEditorScreen: (Long) -> Unit,
    navigateToSubTaskEditorScreen: (Int) -> Unit,
    onEvent: (TaskEditorEvent) -> Unit,
    onBackClick: () -> Unit
) {
    when (taskEditorState) {
        is TaskEditorViewModel.State.Loading -> {}
        is TaskEditorViewModel.State.Ready -> {
            TaskEditorContent(
                task = taskEditorState.task,
                subTasks = taskEditorState.subTasks,
                navigateToTaskEditorScreen = navigateToTaskEditorScreen,
                navigateToSubTaskEditorScreen = navigateToSubTaskEditorScreen,
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
    navigateToTaskEditorScreen: (Long) -> Unit,
    navigateToSubTaskEditorScreen: (Int) -> Unit,
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
        snackbarHost = { SnackbarComponent(snackbarHostState = snackbarHostState) },
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(paddingValues)
        ) {
            TaskEditorComponent(
                task = task,
                navigateToTaskEditorScreen
            )

            SubTasksComponent(
                subTasks = subTasks,
                onEvent = onEvent,
                navigateToSubTaskEditorScreen = navigateToSubTaskEditorScreen
            )
        }
    }
}

@ThemePreviews
@Composable
fun TaskEditorScreenPreview() {
    PlaygroundTheme {
        TaskEditorContent(
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
                    title = "Stuff",
                ),
                SubTask(
                    title = "Do stuff",
                    startDate = LocalDate.now().plusDays(50),
                    endDate = LocalDate.now().plusDays(58),
                    startTime = LocalTime.now(),
                    endTime = LocalTime.now().plusHours(1)
                ),
                SubTask(
                    title = "Do thing",
                    note = "Lorem ipsum dolor sit amet.",
                    startDate = LocalDate.now().plusDays(50),
                    endDate = LocalDate.now().plusDays(58),
                    startTime = LocalTime.now(),
                    endTime = LocalTime.now().plusHours(1)
                ),
                SubTask(
                    title = "Do other thing",
                    note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam blandit porta fringilla. Proin eu odio eget dolor placerat facilisis. Mauris aliquam purus vitae dolor fringilla congue. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.",
                    startDate = LocalDate.now().plusDays(50),
                    endDate = LocalDate.now().plusDays(58),
                    startTime = LocalTime.now(),
                    endTime = LocalTime.now().plusHours(1)
                ),
            ),
            navigateToTaskEditorScreen = {},
            navigateToSubTaskEditorScreen = { _ -> },
            snackbarHostState = SnackbarHostState(),
            onEvent = {},
            onBackPress = {}
        )
    }
}