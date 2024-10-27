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

enum class Route() {
    Main,
    TaskEdit
}

enum class SubScreen(val route: String) {
    TaskEdit("TaskEdit"),
    MainTaskEdit("MainTaskEdit"),
    SubTaskEdit("SubTaskEdit"),
    ExerciseProgram("Exercise Program"),
    ExerciseProgramEdit("Exercise Program Editor"),
}

@Composable
fun rememberPlaygroundAppState(
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    context: Context = LocalContext.current
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
    val navController: NavHostController,
    val drawerState: DrawerState,
    private val context: Context
)