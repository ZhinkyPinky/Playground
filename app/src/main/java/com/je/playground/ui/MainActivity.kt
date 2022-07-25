package com.je.playground.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.whenStarted
import com.je.playground.database.AppDatabase
import com.je.playground.database.tasks.Exercise
import com.je.playground.ui.tasks.TasksScreen
import com.je.playground.ui.tasks.viewmodel.TasksViewModel
import com.je.playground.ui.theme.PlaygroundTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlaygroundTheme {
                // A surface container using the 'background' color from the theme
                PlaygroundApp(this)
            }
        }
    }
}

@Composable
fun PlaygroundApp(activity : MainActivity) {
    val tasksViewModel : TasksViewModel = viewModel(
        factory = TasksViewModel.provideFactory(
            owner = activity
        )
    )
    TasksScreen(tasksViewModel)
}

