package com.je.playground.feature.tasks.editor

import android.util.Log
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.je.playground.navigation.TaskEditorRoute
import kotlinx.serialization.Serializable

const val TASK_EDITOR_ROUTE = "tasks/{taskId}/edit"

@Serializable
object TaskRoute

@Serializable
data class TaskEditorOverview(val taskId: Long = -1L)

fun NavGraphBuilder.taskEditorScreen(
    navController: NavController,
    onEditTaskClick: (Long) -> Unit,
    onEditSubTaskClick: (Long, Int) -> Unit,
    onBackClick: () -> Unit
) {
    composable<TaskEditorOverview> { backStackEntry ->
        val parentEntry =
            remember(backStackEntry) { navController.getBackStackEntry(TaskEditorRoute) }
        TaskEditorScreen(
            viewModel = hiltViewModel(parentEntry),
            onEditTaskClick = onEditTaskClick,
            onEditSubTaskClick = onEditSubTaskClick,
            onBackClick = onBackClick
        )
    }
}




