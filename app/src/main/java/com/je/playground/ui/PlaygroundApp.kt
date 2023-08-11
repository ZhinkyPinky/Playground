package com.je.playground.ui

import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.je.playground.PlaygroundApplication
import com.je.playground.ui.home.HomeScreen
import com.je.playground.ui.taskeditor.TaskEditorScreen
import com.je.playground.ui.tasklist.TasksScreen


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
                startDestination = MainScreen.Home.name,
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

                composable(MainScreen.TaskList.route) { navBackStackEntry ->
                    TasksScreen(
                        tasksViewModelV2 = hiltViewModel(),
                        drawerState = appState.drawerState,
                        navigateToTaskEditWindow = {
                            appState.navigateToTaskEditWindow(navBackStackEntry)
                        }
                    )
                }

                composable(SubScreen.TaskEdit.route) {
                    TaskEditorScreen(
                        taskEditorViewModel = hiltViewModel(),
                        onBackPress = appState::navigateBack
                    )
                }
            }
        }
    }


}