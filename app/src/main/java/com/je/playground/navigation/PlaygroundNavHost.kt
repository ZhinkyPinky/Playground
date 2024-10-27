package com.je.playground.navigation

import TaskEditor
import android.util.Log
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.je.playground.feature.tasks.editor.TaskEditorOverview
import com.je.playground.feature.tasks.editor.taskEditorScreen
import com.je.playground.feature.tasks.list.TaskList
import com.je.playground.feature.tasks.list.taskListScreen
import com.je.playground.ui.PlaygroundAppState
import kotlinx.serialization.Serializable
import mainTaskEditorScreen
import navigateToMainTaskEditor
import navigateToSubTaskEditor
import subTaskEditorScreen
import kotlin.reflect.KClass

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
            navigateToTaskEditScreen = {
                navController.navigate(route = TaskEditorOverview(it))
            }
        )

        navigation<TaskEditorRoute>(
            startDestination = TaskEditorOverview::class
        ) {
            taskEditorScreen(
                navController = navController,
                onEditTaskClick = { navController.navigate(route = TaskEditor) },
                //navController::navigateToMainTaskEditor,
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
    route: KClass<*>
): T {
    //val navGraphRoute = destination.parent?.findStartDestination()?.route ?: return hiltViewModel()
    Log.d("sharedViewModel", route.toString())

    val parentEntry = remember(this) {
        //navController.getBackStackEntry(navGraphRoute)
        navController.getBackStackEntry(route)
    }
    return hiltViewModel(parentEntry)
}