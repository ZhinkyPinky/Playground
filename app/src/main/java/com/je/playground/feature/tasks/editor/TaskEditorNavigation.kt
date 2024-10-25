package com.je.playground.feature.tasks.editor

import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlin.reflect.KFunction2

const val TASK_EDITOR_ROUTE = "tasks/{taskId}/edit"

fun NavController.navigateToTaskEditor(
    taskId: Long
) = navigate(
    route = "tasks/$taskId/edit"
)

fun NavGraphBuilder.taskEditorScreen(
    onEditTaskClick: (Long) -> Unit,
    onEditSubTaskClick: (Long, Int) -> Unit,
    onBackClick: () -> Unit
) {
    composable(
        route = TASK_EDITOR_ROUTE,
        arguments = listOf(navArgument("taskId") { type = NavType.LongType })
    ) {
        TaskEditorScreen(
            viewModel = hiltViewModel(),
            onEditTaskClick = onEditTaskClick,
            onEditSubTaskClick = onEditSubTaskClick,
            onBackClick = onBackClick
        )
    }
}




