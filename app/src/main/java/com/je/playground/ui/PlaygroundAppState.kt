package com.je.playground.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route : String) {
    object Home : Screen("Home")
    object TaskList : Screen("TaskList")
    object TaskEdit : Screen("TaskEdit")
}

@Composable
fun rememberPlaygroundAppState(
    navController : NavHostController = rememberNavController(),
    context : Context = LocalContext.current
) = remember(
    navController,
    context
) {
    PlaygroundAppState(
        navController,
        context
    )
}

class PlaygroundAppState(
    val navController : NavHostController,
    private val context : Context
) {

    fun navigateToTaskEditWindow(from : NavBackStackEntry) {
        if (from.licecycleIsResumed()) {
            navController.navigate(Screen.TaskEdit.route)
        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}

private fun NavBackStackEntry.licecycleIsResumed() = this.lifecycle.currentState == Lifecycle.State.RESUMED
