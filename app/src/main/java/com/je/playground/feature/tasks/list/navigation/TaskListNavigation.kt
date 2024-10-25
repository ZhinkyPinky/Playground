package com.je.playground.feature.tasks.list.navigation

import android.util.Log
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.je.playground.feature.tasks.list.TaskListRoute

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
        TaskListRoute(navigateToTaskEditScreen = {
            navigateToTaskEditScreen(
                it
            )
        })
    }
}