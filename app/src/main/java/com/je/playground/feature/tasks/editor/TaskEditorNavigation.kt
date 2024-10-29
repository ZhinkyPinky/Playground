package com.je.playground.feature.tasks.editor

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.je.playground.navigation.TaskEditorRoute
import kotlinx.serialization.Serializable


@Serializable
data class TaskEditorOverview(val taskId: Long)

fun NavController.navigateToTaskEditorOverview(taskId: Long) =
    navigate(route = TaskEditorOverview(taskId))

fun NavGraphBuilder.taskEditorScreen(
    navController: NavController,
    onEditTaskClick: (Long) -> Unit,
    onEditSubTaskClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    composable<TaskEditorOverview> { backStackEntry ->
        val parentEntry =
            remember(backStackEntry) { navController.getBackStackEntry(TaskEditorRoute) }
        TaskEditorOverviewScreen(
            viewModel = hiltViewModel(parentEntry),
            navigateToTaskEditorScreen = onEditTaskClick,
            navigateToSubTaskEditorScreen = onEditSubTaskClick,
            onBackClick = onBackClick
        )
    }
}




