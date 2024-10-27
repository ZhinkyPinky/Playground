package com.je.playground.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.je.playground.ui.theme.ThemePreviews
import com.je.playground.ui.theme.PlaygroundTheme
import kotlinx.coroutines.launch

@Composable
fun SnackbarComponent(
    snackbarHostState : SnackbarHostState,
    modifier : Modifier = Modifier
) {
    SnackbarHost(
        hostState = snackbarHostState,
    ) { snackbarData ->
        Snackbar(
            snackbarData = snackbarData,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            actionColor = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@ThemePreviews
@Composable
fun SnackbarPreview0() {
    val snackbarHostState = remember { SnackbarHostState() }

    val localCoroutineScope = rememberCoroutineScope()

    LaunchedEffect(
        Unit
    ) {
        localCoroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = "Test",
                duration = SnackbarDuration.Indefinite
            )
        }
    }

    PlaygroundTheme {
        SnackbarComponent(snackbarHostState = snackbarHostState)
    }
}

@ThemePreviews
@Composable
fun SnackbarWithActionPreview0() {
    val snackbarHostState = remember { SnackbarHostState() }

    val localCoroutineScope = rememberCoroutineScope()

    LaunchedEffect(
        Unit
    ) {
        localCoroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = "Test",
                actionLabel = "Action",
                duration = SnackbarDuration.Indefinite
            )
        }
    }

    PlaygroundTheme {
        SnackbarComponent(snackbarHostState = snackbarHostState)
    }
}