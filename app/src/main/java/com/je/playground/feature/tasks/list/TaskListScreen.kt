package com.je.playground.feature.tasks.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.je.playground.R
import com.je.playground.database.tasks.entity.TaskWithSubTasks
import com.je.playground.designsystem.component.task.tasklist.TaskComponent


@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel,
    navigateToTaskEditor: () -> Unit,
    navigateToTaskEditorOverview: (Long) -> Unit,
) {
    val tasksUiState by viewModel.tasksUiState.collectAsState()

    TaskListScreen(
        tasksWithSubTasks = tasksUiState.mainTasksWithSubTasks,
        navigateToTaskEditor = navigateToTaskEditor,
        navigateToTaskEditorOverview = navigateToTaskEditorOverview,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    tasksWithSubTasks: List<TaskWithSubTasks>,
    navigateToTaskEditor: () -> Unit,
    navigateToTaskEditorOverview: (Long) -> Unit,
    onEvent: (TaskListEvent) -> Unit
) {
    var parentHeight by remember { mutableIntStateOf(0) }
    var listHeight by remember { mutableIntStateOf(0) }
    val diffInDp = LocalDensity.current.run { (parentHeight - listHeight).toDp() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.tasks_title)) },
                actions = {
                    IconButton(
                        onClick = { navigateToTaskEditor()},
                        ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add new task",
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
        modifier = Modifier.onSizeChanged {
            parentHeight = it.height
        }
    ) { paddingValues ->
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.padding(paddingValues)) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    contentPadding = PaddingValues(vertical = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .onSizeChanged { listHeight = it.height }
                ) {
                    itemsIndexed(
                        items = tasksWithSubTasks,
                        key = { _, tasksWithSubTasks -> tasksWithSubTasks.task.taskId }) { _, taskWithSubTasks ->
                        if (!taskWithSubTasks.task.isArchived) { //TODO: Just retrieve unarchived???
                            TaskComponent(
                                taskWithSubTasks = taskWithSubTasks,
                                navigateToTaskEditorOverview = navigateToTaskEditorOverview,
                                onEvent = onEvent
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (diffInDp > 0.dp) diffInDp else 0.dp)
                        .background(MaterialTheme.colorScheme.surface)
                )
            }
        }
    }
}
