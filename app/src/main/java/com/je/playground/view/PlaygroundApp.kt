package com.je.playground.view

import androidx.compose.material3.ModalNavigationDrawer
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
import com.je.playground.PlaygroundApplication
import com.je.playground.view.exerciseprogramview.ExerciseProgramListScreen
import com.je.playground.view.exerciseprogramview.ExerciseProgramScreen
import com.je.playground.view.exerciseprogramview.exerciseprogrameditor.ExerciseProgramEditorScreen
import com.je.playground.view.home.HomeScreen
import com.je.playground.view.navigationdrawer.NavigationDrawerContent
import com.je.playground.view.taskview.taskeditor.SubTaskEditorScreen
import com.je.playground.view.taskview.taskeditor.TaskEditorScreen
import com.je.playground.view.taskview.tasklist.TasksScreen


@Composable
fun PlaygroundApp(
    appState : PlaygroundAppState = rememberPlaygroundAppState(),
    application : PlaygroundApplication,
    activity : MainActivity
) {
    ModalNavigationDrawer(
        drawerState = appState.drawerState,
        drawerContent = {
            NavigationDrawerContent(
                appState.drawerState,
                appState.navController
            )
        }
    ) {
        NavHost(
            navController = appState.navController,
            startDestination = MainScreen.TaskList.route
        ) {
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

            composable(MainScreen.TaskList.route) { navBackStackEntry ->
                TasksScreen(
                    tasksViewModel = hiltViewModel(),
                    drawerState = appState.drawerState,
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