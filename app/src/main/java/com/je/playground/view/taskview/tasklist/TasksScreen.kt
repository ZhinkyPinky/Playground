package com.je.playground.view.taskview.tasklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.database.tasks.entity.MainTask
import com.je.playground.database.tasks.entity.MainTaskWithSubTasks
import com.je.playground.view.taskview.tasklist.viewmodel.TasksViewModel


@Composable
fun TasksScreen(
    tasksViewModel : TasksViewModel,
    drawerState : DrawerState,
    navigateToTaskEditScreen : (Long) -> Unit,
) {
    val tasksUiState by tasksViewModel.tasksUiState.collectAsState()

    TasksScreen(
        mainTasksWithSubTasks = tasksUiState.mainTasksWithSubTasks,
        drawerState = drawerState,
        navigateToTaskEditScreen = navigateToTaskEditScreen,
        updateMainTask = tasksViewModel::updateMainTask,
        updateMainTaskWithSubTasks = tasksViewModel::updateMainTaskWithSubTasks,
        deleteMainTaskWithSubTasks = tasksViewModel::deleteMainTaskWithSubTasks,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    mainTasksWithSubTasks : List<MainTaskWithSubTasks>,
    drawerState : DrawerState,
    navigateToTaskEditScreen : (Long) -> Unit,
    updateMainTask : (MainTask) -> Unit,
    updateMainTaskWithSubTasks : (MainTaskWithSubTasks) -> Unit,
    deleteMainTaskWithSubTasks : (MainTaskWithSubTasks) -> Unit,
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
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            itemsIndexed(
                items = mainTasksWithSubTasks,
                key = { _, mainTaskWithSubTasks -> mainTaskWithSubTasks.mainTask.mainTaskId }) { _, mainTaskWithSubTasks ->
                if(!mainTaskWithSubTasks.mainTask.isArchived) {
                    MainTaskComponent(
                        mainTaskWithSubTasks = mainTaskWithSubTasks,
                        navigateToTaskEditScreen = navigateToTaskEditScreen,
                        updateMainTask = updateMainTask,
                        updateMainTaskWithSubTasks = updateMainTaskWithSubTasks,
                        deleteMainTaskWithSubTasks = deleteMainTaskWithSubTasks
                    )
                }
            }
        }
    }
}
