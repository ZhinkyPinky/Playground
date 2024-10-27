package com.je.playground.feature.tasks.list.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.je.playground.feature.tasks.list.TasksScreen

const val TASK_LIST_ROUTE = "tasks"

fun NavController.navigateToTaskList(navOptions: NavOptions) = navigate(
    TASK_LIST_ROUTE,
)

fun NavGraphBuilder.taskListScreen(
    navigateToTaskEditScreen: (Long) -> Unit
) {
    composable(
        route = TASK_LIST_ROUTE,
    ) { navBackStackEntry ->
        TasksScreen(
            viewModel = hiltViewModel(),
            navigateToTaskEditScreen = navigateToTaskEditScreen
        )
    }
}