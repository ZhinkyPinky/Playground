package com.je.playground.ui

import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.je.playground.PlaygroundApplication
import com.je.playground.ui.exerciseprogram.ExerciseProgramScreen
import com.je.playground.ui.home.HomeScreen
import com.je.playground.ui.navigationdrawer.NavigationDrawerContent
import com.je.playground.ui.taskview.taskeditor.TaskEditorScreen
import com.je.playground.ui.taskview.tasklist.TasksScreen


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

                composable(MainScreen.ExerciseProgram.route) { navBackStackEntry ->
                    ExerciseProgramScreen(
                        exerciseProgramViewModel = hiltViewModel(),
                        drawerState = appState.drawerState,
                        navigateToExerciseProgramEditScreen = {}
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