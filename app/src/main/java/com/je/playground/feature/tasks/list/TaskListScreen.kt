package com.je.playground.feature.tasks.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.entity.TaskWithSubTasks
import com.je.playground.designsystem.component.tasklist.TaskComponent


@Composable
internal fun TaskListRoute(
    taskListViewModel : TaskListViewModel = hiltViewModel(),
    navigateToTaskEditScreen : (Long) -> Unit,
) {
    TasksScreen(
        taskListViewModel = taskListViewModel,
        navigateToTaskEditScreen = navigateToTaskEditScreen
    )
}

@Composable
fun TasksScreen(
    taskListViewModel : TaskListViewModel,
    navigateToTaskEditScreen : (Long) -> Unit,
) {
    val tasksUiState by taskListViewModel.tasksUiState.collectAsState()

    TasksScreen(
        tasksWithSubTasks = tasksUiState.mainTasksWithSubTasks,
        navigateToTaskEditScreen = navigateToTaskEditScreen,
        updateTask = taskListViewModel::updateMainTask,
        updateTaskWithSubTasks = taskListViewModel::updateMainTaskWithSubTasks,
        onEvent = taskListViewModel::onEvent
    )
}

@Composable
fun TasksScreen(
    tasksWithSubTasks : List<TaskWithSubTasks>,
    navigateToTaskEditScreen : (Long) -> Unit,
    updateTask : (Task) -> Unit,
    updateTaskWithSubTasks : (TaskWithSubTasks) -> Unit,
    onEvent : (TaskListEvent) -> Unit
) {
    Scaffold(
        topBar = { TasksScreenTopBar(navigateToTaskEditScreen) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            taskList(
                tasksWithSubTasks = tasksWithSubTasks,
                navigateToTaskEditScreen = navigateToTaskEditScreen,
                updateTask = updateTask,
                updateTaskWithSubTasks = updateTaskWithSubTasks,
                onEvent = onEvent
            )
        }
    }
}

fun LazyListScope.taskList(
    tasksWithSubTasks : List<TaskWithSubTasks>,
    navigateToTaskEditScreen : (Long) -> Unit,
    updateTask : (Task) -> Unit,
    updateTaskWithSubTasks : (TaskWithSubTasks) -> Unit,
    onEvent : (TaskListEvent) -> Unit
) {
    itemsIndexed(
        items = tasksWithSubTasks,
        key = { _, tasksWithSubTasks -> tasksWithSubTasks.task.mainTaskId }) { _, taskWithSubTasks ->
        if (!taskWithSubTasks.task.isArchived) {
            TaskComponent(
                taskWithSubTasks = taskWithSubTasks,
                navigateToTaskEditScreen = navigateToTaskEditScreen,
                updateTask = updateTask,
                updateTaskWithSubTasks = updateTaskWithSubTasks,
                onEvent = onEvent
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreenTopBar(navigateToTaskEditScreen : (Long) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    Column {
        TopAppBar(
            title = {
                Text(
                    text = "Tasks",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 22.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Start,

                    modifier = Modifier
                        .padding(
                            start = 0.dp,
                            top = 4.dp,
                            end = 8.dp,
                            bottom = 4.dp
                        )
                        .weight(1f)
                )
            },
            actions = {
                IconButton(
                    onClick = { navigateToTaskEditScreen(0L) }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add new task",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )

        Divider(
            color = MaterialTheme.colorScheme.background
        )
    }
}
