package com.je.playground.view

import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
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
            startDestination = Route.Main.name
        ) {
            navigation(
                startDestination = MainScreen.TaskList.route,
                route = Route.Main.name
            ) {
                composable(MainScreen.Home.route) { navBackStackEntry ->
                    HomeScreen(
                        homeViewModel = hiltViewModel(),
                        drawerState = appState.drawerState,
                        navigateToTaskScreen = {
                            appState.navigateToTaskScreen(navBackStackEntry)
                        }
                    )
                }

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

                composable(
                    route = SubScreen.TaskEdit.route + "?mainTaskId={mainTaskId}",
                    arguments = listOf(navArgument("mainTaskId") { defaultValue = 0L })
                ) {
                    TaskEditorScreen(
                        mainTaskId = it.arguments?.getLong("mainTaskId"),
                        tasksViewModel = hiltViewModel(),
                        onBackPress = appState::navigateBack
                    )
                }
            }
        }
    }


}