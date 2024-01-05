package com.je.playground.view

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

enum class Route() {
    Main,
    TaskEdit
}

enum class MainScreen(val route : String) {
    Home("Home"),
    ExerciseProgramList("Exercise Program List"),
    Habits("Habits"),
    TaskList("Tasks"),
    Settings("Settings")
}

enum class SubScreen(val route : String) {
    TaskEdit("TaskEdit"),
    SubTaskEdit("SubTaskEdit"),
    ExerciseProgram("Exercise Program"),
    ExerciseProgramEdit("Exercise Program Editor"),
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

    fun navigateToTaskEditScreen(
        from : NavBackStackEntry,
        mainTaskId : Long
    ) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(SubScreen.TaskEdit.route + "?mainTaskId=$mainTaskId")
        }
    }

    fun navigateToSubTaskEditorScreen(
        from : NavBackStackEntry,
        subTaskId : Long
    ) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(SubScreen.SubTaskEdit.route + "?subTaskId=$subTaskId")
        }
    }

    fun navigateToHabitScreen(from : NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainScreen.Habits.route)
        }
    }


    fun navigateToExerciseProgramEditScreen(
        from : NavBackStackEntry,
        exerciseProgramId : Any
    ) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(SubScreen.ExerciseProgramEdit.route + "?exerciseProgramId=$exerciseProgramId")
        }
    }

    fun navigateToExerciseProgramScreen(
        from : NavBackStackEntry,
        exerciseProgramId : Long
    ) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(SubScreen.ExerciseProgram.route + "?exerciseProgramId=$exerciseProgramId")
        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}

private fun NavBackStackEntry.lifecycleIsResumed() = this.lifecycle.currentState == Lifecycle.State.RESUMED
