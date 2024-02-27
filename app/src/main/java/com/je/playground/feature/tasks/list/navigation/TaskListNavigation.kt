package com.je.playground.feature.tasks.list.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.je.playground.feature.tasks.list.TaskListRoute

const val TASK_LIST_ROUTE = "task_list"

fun NavController.navigateToTaskList(navOptions : NavOptions) = navigate(
    TASK_LIST_ROUTE,
    navOptions
)

fun NavGraphBuilder.taskListScreen(navigateToTaskEditScreen : (NavBackStackEntry, Long) -> Unit) {
    composable(
        route = TASK_LIST_ROUTE,
    ) { navBackStackEntry ->
        TaskListRoute(navigateToTaskEditScreen = {
            navigateToTaskEditScreen(
                navBackStackEntry,
                it
            )
        })
    }
}