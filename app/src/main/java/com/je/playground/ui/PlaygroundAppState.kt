package com.je.playground.ui

import android.content.Context
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


/*
sealed class MainScreen(val route : String) {
    object Home : SubScreen("Home")
    object Habits : SubScreen("Habits")
    object TaskList : SubScreen("TaskList")
}

sealed class SubScreen(val route : String) {
    object TaskEdit : SubScreen("TaskEdit")
}
 */

enum class Route() {
    Main
}

enum class MainScreen(val route : String) {
    Home("Home"),
    ExerciseProgram("Exercise Program"),
    Habits("Habits"),
    TaskList("Tasks"),
    Settings("Settings")
}

enum class SubScreen(val route : String) {
    TaskEdit("TaskEdit")
}

@Composable
fun rememberPlaygroundAppState(
    navController : NavHostController = rememberNavController(),
    drawerState : DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    context : Context = LocalContext.current
) = remember(
    navController,
    drawerState,
    context
) {
    PlaygroundAppState(
        navController,
        drawerState,
        context
    )
}

class PlaygroundAppState(
    val navController : NavHostController,
    val drawerState : DrawerState,
    private val context : Context
) {
    fun navigateToHomeScreen() {
        navController.navigate(MainScreen.Home.route)
    }

    fun navigateToTaskScreen(from : NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainScreen.TaskList.route)
        }
    }

    fun navigateToTaskEditWindow(from : NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(SubScreen.TaskEdit.route)
        }
    }

    fun navigateToHabitScreen(from : NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainScreen.Habits.route)
        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }


}

private fun NavBackStackEntry.lifecycleIsResumed() = this.lifecycle.currentState == Lifecycle.State.RESUMED
