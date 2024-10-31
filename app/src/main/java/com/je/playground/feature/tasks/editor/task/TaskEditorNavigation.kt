package com.je.playground.feature.tasks.editor.task

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.je.playground.feature.tasks.editor.overview.navigateToTaskEditorOverview
import com.je.playground.navigation.TaskEditorRoute
import kotlinx.serialization.Serializable

@Serializable
data class TaskEditor(val taskId: Long)

fun NavController.navigateToTaskEditor(taskId: Long = 0) =
    navigate(route = TaskEditor(taskId))

fun NavGraphBuilder.taskEditorScreen(
    navController: NavController,
) {
    composable<TaskEditor> {
        TaskEditorScreen(
            viewModel = hiltViewModel(),
            onBackClick = navController::navigateUp,
            onSave = {
                navController.navigateToTaskEditorOverview(it) { popUpTo(TaskEditorRoute) }
            }
        )
    }
}
