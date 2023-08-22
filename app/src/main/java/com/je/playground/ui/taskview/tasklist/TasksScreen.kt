package com.je.playground.ui.taskview.tasklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
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
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.database.tasks.entity.MainTaskWithSubTasks
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.ui.taskview.viewmodel.TasksViewModel
import kotlinx.coroutines.launch


@Composable
fun TasksScreen(
    navigateToTaskEditWindow : () -> Unit,
    drawerState : DrawerState,
    tasksViewModel : TasksViewModel
) {
    val tasksUiState by tasksViewModel.tasksUiState.collectAsState()

    TasksScreen(
        //tasksUiState = tasksUiState,
        taskGroupsWithTasks = tasksUiState.taskGroupsWithSubTasks,
        /*
        listOf(
            MainTaskWithSubTasks(
                mainTask = MainTask(
                    mainTaskId = 0L,
                    title = "TestGroup",
                    type = TaskTypeV2.RegularTask.ordinal,
                    priority = Priority.Medium.ordinal
                ),
                subTasks = mutableListOf(
                    SubTask(
                        mainTaskId = 0L,
                        subTaskId = 0L,
                        title = "TestTask",
                    ),
                    SubTask(
                        mainTaskId = 0L,
                        subTaskId = 1L,
                        title = "TestTask",
                        note = "The quick brown fox jumped over the lazy dog." +
                                "The quick brown fox jumped over the lazy dog." +
                                "The quick brown fox jumped over the lazy dog." +
                                "The quick brown fox jumped over the lazy dog." +
                                "The quick brown fox jumped over the lazy dog." +
                                "The quick brown fox jumped over the lazy dog."
                    ),
                    SubTask(
                        mainTaskId = 0L,
                        subTaskId = 2L,
                        title = "TestTask"
                    ),
                )
            ),
            MainTaskWithSubTasks(
                mainTask = MainTask(
                    mainTaskId = 1L,
                    title = "TestGroup",
                    type = TaskTypeV2.RegularTask.ordinal,
                    priority = Priority.Medium.ordinal
                ),
                subTasks = mutableListOf(
                    SubTask(
                        mainTaskId = 1L,
                        subTaskId = 4L,
                        title = "TestTask"
                    )
                )
            )
        )
        */
        drawerState = drawerState,
        navigateToTaskEditWindow = navigateToTaskEditWindow,
        deleteTask = { },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    taskGroupsWithTasks : List<MainTaskWithSubTasks>,
    drawerState : DrawerState,
    navigateToTaskEditWindow : () -> Unit,
    deleteTask : (SubTask) -> Unit,
) {
    Scaffold(
        topBar = {
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
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { navigateToTaskEditWindow() }) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add new task",
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }

                        IconButton(onClick = {/* TODO */ }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Config",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(end = 2.dp)
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
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(it)
        ) {
            items(items = taskGroupsWithTasks) { taskGroupWithTasks ->
                key(taskGroupWithTasks.mainTask.mainTaskId) {
                    TaskGroupComponent(
                        mainTaskWithSubTasks = taskGroupWithTasks,
                        deleteTask = {}
                    )
                }
            }
        }
    }
}
