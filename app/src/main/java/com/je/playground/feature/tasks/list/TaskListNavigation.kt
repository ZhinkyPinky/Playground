package com.je.playground.feature.tasks.list

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object TaskList

fun NavGraphBuilder.taskListScreen(
    navigateToTaskEditorOverview: (Long) -> Unit
) {
    composable<TaskList> {
        TasksScreen(
            viewModel = hiltViewModel(),
            navigateToTaskEditorOverview= navigateToTaskEditorOverview
        )
    }
}