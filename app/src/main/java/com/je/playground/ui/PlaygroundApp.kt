package com.je.playground.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.je.playground.PlaygroundApplication
import com.je.playground.ui.taskeditor.TaskEditorScreen
import com.je.playground.ui.tasklist.TasksScreen
import com.je.playground.ui.tasklist.viewmodel.TasksViewModelV2
import dagger.hilt.android.AndroidEntryPoint


@Composable
fun PlaygroundApp(
    tasksViewModelV2 : TasksViewModelV2,
    appState : PlaygroundAppState = rememberPlaygroundAppState(),
    application : PlaygroundApplication,
    activity : MainActivity
) {

    /*
    val tasksViewModel : TasksViewModelV2 = viewModel(
        factory = TasksViewModelV2.provideFactory(
            application = application,
            owner = activity
        )
    )
     */

    NavHost(
        navController = appState.navController,
        startDestination = Screen.TaskList.route
    ) {
        composable(Screen.TaskList.route) { navBackStackEntry ->
            TasksScreen(
                tasksViewModelV2 = tasksViewModelV2,
                navigateToTaskEditWindow = {
                    appState.navigateToTaskEditWindow(navBackStackEntry)
                }
            )
        }
        composable(Screen.TaskEdit.route) { navBackStackEntry ->
            TaskEditorScreen(
                tasksViewModelV2 = tasksViewModelV2,
                onBackPress = appState::navigateBack
            )
        }
    }
}