package com.je.playground.feature.tasks.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.entity.TaskWithSubTasks
import com.je.playground.database.tasks.repository.TaskRepository
import com.je.playground.feature.tasks.domain.DeleteTaskUseCase
import com.je.playground.feature.tasks.domain.GetActiveTasksWithSubTasksUseCase
import com.je.playground.feature.tasks.domain.ToggleSubTaskCompletionUseCase
import com.je.playground.feature.tasks.domain.ToggleTaskCompletionUseCase
import com.je.playground.notification.NotificationScheduler
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
    application : Application,
    private val taskRepository : TaskRepository,
    private val getActiveTasksWithSubTasksUseCase : GetActiveTasksWithSubTasksUseCase,
    private val deleteTask : DeleteTaskUseCase,
    private val toggleTaskCompletion : ToggleTaskCompletionUseCase,
    private val toggleSubTaskCompletion : ToggleSubTaskCompletionUseCase
) : ViewModel() {
    private val _tasksUiState = MutableStateFlow(TasksUiState())

    val tasksUiState : StateFlow<TasksUiState>
        get() = _tasksUiState

    private val notification = NotificationScheduler(application.applicationContext)

    init {
        viewModelScope.launch {
            getActiveTasksWithSubTasksUseCase()
                .collect {
                    _tasksUiState.value = TasksUiState(it)
                }
        }

    }

    fun onEvent(event : TaskListEvent) {
        when (event) {
            is TaskListEvent.DeleteTaskWithSubTasks -> deleteTask(event.taskWithSubTasks)
            is TaskListEvent.ToggleTaskCompletion -> viewModelScope.launch { toggleTaskCompletion(event.task) }
            is TaskListEvent.ToggleSubTaskCompletion -> viewModelScope.launch { toggleSubTaskCompletion(event.subTask) }
        }
    }

    fun updateMainTaskWithSubTasks(taskWithSubTasks : TaskWithSubTasks) {
        taskRepository.updateMainTask(taskWithSubTasks.task)
        taskRepository.updateSubTasks(taskWithSubTasks.subTasks)
    }

    fun updateMainTask(task : Task) {
        taskRepository.updateMainTask(task)

        if (task.isArchived) {
            notification.cancelNotification(task.mainTaskId.toInt())
        }
    }
}


