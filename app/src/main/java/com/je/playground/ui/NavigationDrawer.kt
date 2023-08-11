package com.je.playground.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChecklistRtl
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawerContent(
    drawerState : DrawerState,
    navController : NavHostController
) {
    val mainScreens = MainScreen.values()
    ModalDrawerSheet(
        drawerShape = MaterialTheme.shapes.large.copy(CornerSize(0)),
        drawerContainerColor = MaterialTheme.colors.primary,
    ) {
        LazyColumn() {
            items(count = mainScreens.size) {
                NavigationDrawerItem(
                    mainScreen = mainScreens[it],
                    drawerState = drawerState,
                    navController = navController
                )
            }
        }
    }
}


@Composable
fun NavigationDrawerItem(
    mainScreen : MainScreen,
    drawerState : DrawerState,
    navController : NavController
) {
    val coroutineScope = rememberCoroutineScope()
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = when (mainScreen) {
                    MainScreen.Home -> Icons.Filled.Home
                    MainScreen.Habits -> Icons.Filled.List
                    MainScreen.TaskList -> Icons.Filled.ChecklistRtl
                    MainScreen.Settings -> Icons.Filled.Settings
                },
                contentDescription = mainScreen.route,
                tint = MaterialTheme.colors.secondary
            )
        },
        label = {
            Text(
                text = mainScreen.route,
                color = MaterialTheme.colors.secondary,
                fontSize = 22.sp,
                maxLines = 1,
                textAlign = TextAlign.Start
            )
        },
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colors.primary,
            unselectedContainerColor = MaterialTheme.colors.primary
        ),
        selected = false,
        onClick = {
            navController.navigate(mainScreen.route) {
                popUpTo(Route.Main.name)
            }

            coroutineScope.launch {
                drawerState.close()
            }
        })
}

