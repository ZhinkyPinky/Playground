package com.je.playground.navigation

import com.je.playground.feature.tasks.editor.task.TaskEditor
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.je.playground.feature.tasks.editor.TaskEditorOverview
import com.je.playground.feature.tasks.editor.navigateToTaskEditorOverview
import com.je.playground.feature.tasks.editor.taskEditorScreen
import com.je.playground.feature.tasks.list.TaskList
import com.je.playground.feature.tasks.list.taskListScreen
import com.je.playground.ui.PlaygroundAppState
import kotlinx.serialization.Serializable
import com.je.playground.feature.tasks.editor.task.mainTaskEditorScreen
import navigateToSubTaskEditor
import subTaskEditorScreen

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
        taskListScreen(navigateToTaskEditorOverview = navController::navigateToTaskEditorOverview)

        navigation<TaskEditorRoute>(
            startDestination = TaskEditorOverview::class
        ) {
            taskEditorScreen(
                navController = navController,
                onEditTaskClick = { navController.navigate(route = TaskEditor) },
                //navController::com.je.playground.feature.tasks.editor.task.navigateToMainTaskEditor,
                onEditSubTaskClick = navController::navigateToSubTaskEditor,
                onBackClick = navController::navigateUp
            )

            mainTaskEditorScreen(navController)

            subTaskEditorScreen(navController)
        }

    }
}

