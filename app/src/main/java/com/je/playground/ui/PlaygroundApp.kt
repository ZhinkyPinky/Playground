package com.je.playground.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.je.playground.designsystem.component.PlaygroundBackground
import com.je.playground.navigation.PlaygroundNavHost


@Composable
fun PlaygroundApp(
    appState: PlaygroundAppState = rememberPlaygroundAppState(),
) {
    PlaygroundBackground {
        ModalNavigationDrawer(
            drawerState = appState.drawerState,
            drawerContent = {
                NavigationDrawerContent(
                    appState.drawerState,
                    appState.navController
                )
            }
        ) {
            PlaygroundNavHost(appState = appState)
        }
    }
}

