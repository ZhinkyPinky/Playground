package com.je.playground.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.je.playground.navigation.TopLevelDestinations
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawerContent(
    drawerState : DrawerState,
    navController : NavHostController
) {
    val topLevelDestinations = TopLevelDestinations.entries.toTypedArray()
    ModalDrawerSheet(
        drawerShape = MaterialTheme.shapes.large.copy(CornerSize(0)),
        drawerContainerColor = MaterialTheme.colorScheme.primary,
    ) {
        LazyColumn() {
            items(count = topLevelDestinations.size) {
                NavigationDrawerItem(
                    topLevelDestination = topLevelDestinations[it],
                    drawerState = drawerState,
                    navController = navController
                )
            }
        }
    }
}


@Composable
fun NavigationDrawerItem(
    topLevelDestination : TopLevelDestinations,
    drawerState : DrawerState,
    navController : NavController
) {
    val coroutineScope = rememberCoroutineScope()
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = topLevelDestination.selectedIcon,
                contentDescription = topLevelDestination.name,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        },
        label = {
            Text(
                text = stringResource(id = topLevelDestination.iconTextId),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 22.sp,
                maxLines = 1,
                textAlign = TextAlign.Start
            )
        },
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            unselectedContainerColor = MaterialTheme.colorScheme.primary
        ),
        selected = false,
        onClick = {
            navController.navigate(topLevelDestination.name) {
                popUpTo(Route.Main.name)
            }

            coroutineScope.launch {
                drawerState.close()
            }
        })
}

