package com.je.playground.ui.tasks.viewmodel

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.je.playground.Graph
import com.je.playground.Graph.tasksRepository
import com.je.playground.database.repository.TasksRepository
import com.je.playground.database.tasks.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TasksUiState(
    val simpleTasks : List<SimpleTask> = emptyList(),
    val exercises : List<Exercise> = emptyList(),
    val exerciseProgramExerciseConnection : List<ExerciseProgramExerciseConnection> = emptyList(),
    val exerciseProgramWithExercises : List<ExerciseProgramWithExercises> = emptyList(),
    val exerciseProgramsWithExercisesAndConnections : List<ExerciseProgramWithExercisesAndConnections> = emptyList()
)

class TasksViewModel(
    tasksRepository : TasksRepository
) : ViewModel() {
    private val _tasksUiState = MutableStateFlow(TasksUiState())

    val tasksUiState : StateFlow<TasksUiState>
        get() = _tasksUiState

    init {
        viewModelScope.launch {
            combine(
                tasksRepository.getSimpleTasks(),
                tasksRepository.getExercises(),
                tasksRepository.getExerciseProgramExerciseConnections(),
                tasksRepository.getExerciseProgramsWithExercises(),
                tasksRepository.getExerciseProgramsWithExercisesAndConnection()
            ) { simpleTasks, exercises, exerciseProgramExerciseConnection, exerciseProgramWithExercises, exerciseProgramWithExercisesAndConnection ->
                TasksUiState(
                    simpleTasks = simpleTasks,
                    exercises = exercises,
                    exerciseProgramExerciseConnection = exerciseProgramExerciseConnection,
                    exerciseProgramWithExercises = exerciseProgramWithExercises,
                    exerciseProgramsWithExercisesAndConnections = exerciseProgramWithExercisesAndConnection
                )
            }
                .catch { throwable ->
                    throw throwable
                }
                .collect {
                    _tasksUiState.value = it
                }
        }
    }

    fun updateExerciseProgramExerciseConnection(exerciseProgramExerciseConnection : ExerciseProgramExerciseConnection) {
        viewModelScope.launch {
            tasksRepository.updateExerciseProgramExerciseConnection(exerciseProgramExerciseConnection)
        }
    }

    companion object {
        fun provideFactory(
            tasksRepository : TasksRepository = Graph.tasksRepository,
            owner : SavedStateRegistryOwner,
            defaultArgs : Bundle? = null,
        ) : AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory(
                owner,
                defaultArgs
            ) {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key : String,
                    modelClass : Class<T>,
                    handle : SavedStateHandle
                ) : T {
                    return TasksViewModel(tasksRepository) as T
                }
            }
    }
}