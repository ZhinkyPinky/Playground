package com.je.playground.navigation

import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.je.playground.feature.tasks.editor.overview.TaskEditorOverview
import com.je.playground.feature.tasks.editor.overview.navigateToTaskEditorOverview
import com.je.playground.feature.tasks.editor.overview.taskEditorOverviewScreen
import com.je.playground.feature.tasks.editor.task.navigateToTaskEditor
import com.je.playground.feature.tasks.list.TaskList
import com.je.playground.feature.tasks.list.taskListScreen
import com.je.playground.ui.PlaygroundAppState
import kotlinx.serialization.Serializable
import com.je.playground.feature.tasks.editor.task.taskEditorScreen
import com.je.playground.feature.tasks.editor.subTask.subTaskEditorScreen

@Serializable
object TaskEditorRoute

@Composable
fun PlaygroundNavHost(
    appState: PlaygroundAppState
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = TaskList,
        modifier = Modifier.imePadding()
    ) {
        taskListScreen(
            navigateToTaskEditor = navController::navigateToTaskEditor,
            navigateToTaskEditorOverview = navController::navigateToTaskEditorOverview
        )

        navigation<TaskEditorRoute>(
            startDestination = TaskEditorOverview::class
        ) {
            taskEditorOverviewScreen(navController = navController)
            taskEditorScreen(navController = navController)
            subTaskEditorScreen(navController = navController)
        }
    }
}

