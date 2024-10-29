package com.je.playground.feature.tasks.editor.task

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.je.playground.navigation.TaskEditorRoute
import kotlinx.serialization.Serializable

@Serializable
object TaskEditor

fun NavGraphBuilder.mainTaskEditorScreen(
    navController: NavController,
) {
    composable<TaskEditor> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(TaskEditorRoute)
        }
        
        MainTaskEditorScreen(
            viewModel = hiltViewModel(parentEntry),
            onBackClick = navController::navigateUp
        )
    }
}
