package com.je.playground.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.je.playground.PlaygroundApplication
import com.je.playground.ui.theme.PlaygroundTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlaygroundTheme {
                // A surface container using the 'background' color from the theme
                PlaygroundApp(
                    application = application as PlaygroundApplication,
                    activity = this
                )
            }
        }
    }
}

/*
@Composable
fun PlaygroundApp(
    application : PlaygroundApplication,
    activity : MainActivity
) {
    val tasksViewModelV2 : TasksViewModelV2 = viewModel(
        factory = TasksViewModelV2.provideFactory(
            application = application,
            owner = activity
        )
    )
    TasksScreen(tasksViewModelV2)
}
 */
