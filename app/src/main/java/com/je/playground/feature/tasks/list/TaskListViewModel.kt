package com.je.playground.feature.tasks.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.entity.TaskWithSubTasks
import com.je.playground.feature.tasks.domain.DeleteTaskUseCase
import com.je.playground.feature.tasks.domain.GetActiveTasksWithSubTasksUseCase
import com.je.playground.feature.tasks.domain.ToggleSubTaskIsCompletedUseCase
import com.je.playground.feature.tasks.domain.ToggleTaskIsArchivedUseCase
import com.je.playground.feature.tasks.domain.ToggleTaskIsCompletedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class TasksUiState(
    val mainTasksWithSubTasks : List<TaskWithSubTasks> = emptyList(),
)

enum class TaskType(type : String) {
    SimpleTask("Basic Task"),
    ExerciseProgram("Exercise Program")
}

enum class TaskTypeV2(type : String) {
    RegularTask("Regular Task"),
    ExerciseTask("Exercise Task"),
    HabitTask("Habit Task"),
    ShoppingTask("Shopping Task")
}

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getActiveTasksWithSubTasks : GetActiveTasksWithSubTasksUseCase,
    private val deleteTask : DeleteTaskUseCase,
    private val toggleTaskIsCompleted : ToggleTaskIsCompletedUseCase,
    private val toggleSubTaskIsCompleted : ToggleSubTaskIsCompletedUseCase,
    private val toggleTaskIsArchived : ToggleTaskIsArchivedUseCase
) : ViewModel() {
    sealed class State{
        data object Loading : State()
        data class Ready(
            val task : Task,
            val subTasks : List<SubTask>,
            val completedSubTasks : Int        )
    }
    private val _tasksUiState = MutableStateFlow(TasksUiState())

    val tasksUiState : StateFlow<TasksUiState>
        get() = _tasksUiState

    init {
        viewModelScope.launch {
            getActiveTasksWithSubTasks()
                .collect {
                    _tasksUiState.value = TasksUiState(it)
                }
        }
    }

    fun onEvent(event : TaskListEvent) {
        when (event) {
                   is TaskListEvent.DeleteTaskWithSubTasks -> deleteTask(event.taskWithSubTasks)
                   is TaskListEvent.ToggleTaskCompletion -> viewModelScope.launch { toggleTaskIsCompleted(event.task) }
                   is TaskListEvent.ToggleSubTaskCompletion -> viewModelScope.launch { toggleSubTaskIsCompleted(event.subTask) }
                   is TaskListEvent.ToggleTaskArchived -> toggleTaskIsArchived(event.task)
        }
    }
}


