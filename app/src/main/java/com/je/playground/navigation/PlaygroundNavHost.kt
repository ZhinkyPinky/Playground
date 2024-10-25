package com.je.playground.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.je.playground.feature.tasks.editor.navigateToTaskEditor
import com.je.playground.feature.tasks.editor.taskEditorScreen
import com.je.playground.feature.tasks.list.navigation.TASK_LIST_ROUTE
import com.je.playground.feature.tasks.list.navigation.taskListScreen
import com.je.playground.ui.PlaygroundAppState
import mainTaskEditorScreen
import navigateToMainTaskEditor
import navigateToSubTaskEditor
import subTaskEditorScreen


@Composable
fun PlaygroundNavHost(appState: PlaygroundAppState) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = TASK_LIST_ROUTE
    ) {
        taskListScreen(navController::navigateToTaskEditor)

        navigation(
            startDestination = "tasks/{taskId}/edit",
            route = "edit_task"
        ) {
            taskEditorScreen(
                onEditTaskClick = navController::navigateToMainTaskEditor,
                onEditSubTaskClick = navController::navigateToSubTaskEditor,
                onBackClick = navController::navigateUp
            )

            mainTaskEditorScreen(navController)

            subTaskEditorScreen(navController)
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
    route: String
): T {
    //val navGraphRoute = destination.parent?.findStartDestination()?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        //navController.getBackStackEntry(navGraphRoute)
        navController.getBackStackEntry(route)
    }
    return hiltViewModel(parentEntry)
}