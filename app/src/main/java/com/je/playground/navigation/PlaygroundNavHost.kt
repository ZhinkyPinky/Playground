package com.je.playground.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.je.playground.designsystem.component.taskeditor.MainTaskEditorScreen
import com.je.playground.designsystem.component.taskeditor.SubTaskEditorScreen
import com.je.playground.feature.tasks.editor.TaskEditorScreen
import com.je.playground.feature.tasks.list.TasksScreen
import com.je.playground.feature.tasks.list.navigation.TASK_LIST_ROUTE
import com.je.playground.feature.tasks.list.navigation.taskListScreen
import com.je.playground.ui.MainScreen
import com.je.playground.ui.PlaygroundAppState
import com.je.playground.ui.Route
import com.je.playground.ui.SubScreen


@Composable
fun PlaygroundNavHost(appState : PlaygroundAppState) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = TASK_LIST_ROUTE
    ) {
        /*
        composable(MainScreen.ExerciseProgramList.route) { navBackStackEntry ->
            ExerciseProgramListScreen(
                exerciseProgramViewModel = hiltViewModel(),
                drawerState = appState.drawerState,
                navigateToExerciseProgramScreen = {
                    appState.navigateToExerciseProgramScreen(
                        navBackStackEntry,
                        it
                    )
                },
                navigateToExerciseProgramEditScreen = {
                    appState.navigateToExerciseProgramEditScreen(
                        navBackStackEntry,
                        it
                    )
                },
            )
        }
        composable(MainScreen.Home.route) { navBackStackEntry ->
            HomeScreen(
                homeViewModel = hiltViewModel(),
                drawerState = appState.drawerState,
                navigateToTaskScreen = {
                    appState.navigateToTaskScreen(navBackStackEntry)
                }
            )
        }

        composable(SubScreen.ExerciseProgram.route) { navBackStackEntry ->
            ExerciseProgramScreen(
                exerciseProgramViewModel = hiltViewModel(),
                drawerState = appState.drawerState,
                navigateToExerciseProgramEditScreen = {
                    appState.navigateToExerciseProgramEditScreen(
                        navBackStackEntry,
                        it
                    )
                }
            )
        }

        composable(
            route = SubScreen.ExerciseProgramEdit.route,
            arguments = listOf(navArgument("exerciseProgramId") { defaultValue = 0L })
        ) {
            ExerciseProgramEditorScreen(
                exerciseProgramId = it.arguments?.getLong("exerciseProgramId"),
                exerciseProgramViewModel = hiltViewModel(),
                onBackPress = appState::navigateBack
            )
        }

         */

        taskListScreen(appState::navigateToTaskEditScreen)

        //taskEditorScreen()

        composable(MainScreen.TaskList.route) { navBackStackEntry ->
            TasksScreen(
                taskListViewModel = hiltViewModel(),
                navigateToTaskEditScreen = {
                    appState.navigateToTaskEditScreen(
                        navBackStackEntry,
                        it
                    )
                }
            )
        }

        navigation(
            startDestination = SubScreen.TaskEdit.route + "?mainTaskId={mainTaskId}",
            route = Route.TaskEdit.name
        ) {
            composable(
                route = SubScreen.TaskEdit.route + "?mainTaskId={mainTaskId}",
                arguments = listOf(navArgument("mainTaskId") { defaultValue = 0L })
            ) { navBackStackEntry ->
                TaskEditorScreen(
                    taskEditorViewModel = hiltViewModel(),
                    navigateToMainTaskEditorScreen = {
                        appState.navigateToMainTaskEditorScreen(navBackStackEntry)
                    },
                    navigateToSubTaskEditorScreen = {
                        appState.navigateToSubTaskEditorScreen(
                            navBackStackEntry,
                            it
                        )
                    },
                    onBackPress = appState::navigateBack
                )
            }

            composable(
                route = SubScreen.MainTaskEdit.route,
            ) { navBackStackEntry ->
                MainTaskEditorScreen(
                    taskEditorViewModel = navBackStackEntry.sharedViewModel(navController = appState.navController),
                    onBackPress = appState::navigateBack
                )
            }

            composable(
                route = SubScreen.SubTaskEdit.route + "?subTaskIndex={subTaskIndex}",
                arguments = listOf(navArgument("subTaskIndex") { defaultValue = -1 })
            ) { navBackStackEntry ->
                SubTaskEditorScreen(
                    subTaskIndex = navBackStackEntry.arguments!!.getInt("subTaskIndex"),
                    taskEditorViewModel = navBackStackEntry.sharedViewModel(navController = appState.navController),
                    onBackPress = appState::navigateBack
                )
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController : NavController,
) : T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}