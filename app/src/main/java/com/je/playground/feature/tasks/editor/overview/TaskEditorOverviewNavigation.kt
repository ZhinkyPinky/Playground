package com.je.playground.feature.tasks.editor.overview

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.je.playground.feature.tasks.editor.task.navigateToTaskEditor
import kotlinx.serialization.Serializable
import com.je.playground.feature.tasks.editor.subTask.navigateToSubTaskEditor


@Serializable
data class TaskEditorOverview(val taskId: Long)

fun NavController.navigateToTaskEditorOverview(taskId: Long = 0) =
    navigate(route = TaskEditorOverview(taskId))

fun NavController.navigateToTaskEditorOverview(
    taskId: Long = 0,
    builder: NavOptionsBuilder.() -> Unit
) = navigate(route = TaskEditorOverview(taskId), builder = builder)

fun NavGraphBuilder.taskEditorOverviewScreen(
    navController: NavController,
) {
    composable<TaskEditorOverview> {
        TaskEditorOverviewScreen(
            viewModel = hiltViewModel(),
            navigateToTaskEditor = navController::navigateToTaskEditor,
            navigateToSubTaskEditor = navController::navigateToSubTaskEditor,
            onBackClick = navController::navigateUp
        )
    }
}




