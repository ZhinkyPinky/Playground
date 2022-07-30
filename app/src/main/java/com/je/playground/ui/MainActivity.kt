package com.je.playground.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.je.playground.GraphV2
import com.je.playground.PlaygroundApplication
import com.je.playground.ui.tasklist.TasksScreen
import com.je.playground.ui.tasklist.viewmodel.TasksViewModelV2
import com.je.playground.ui.theme.PlaygroundTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlaygroundTheme {
                // A surface container using the 'background' color from the theme
                PlaygroundApp(
                    application as PlaygroundApplication,
                    this
                )
            }
        }
    }
}

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

