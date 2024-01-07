package com.je.playground.view.taskview.tasklist.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.je.playground.database.tasks.entity.MainTask
import com.je.playground.database.tasks.entity.MainTaskWithSubTasks
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.repository.TasksRepository
import com.je.playground.notification.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TasksUiState(
    val subTasks : List<SubTask> = emptyList(),
    val mainTasks : List<MainTask> = emptyList(),
    val mainTasksWithSubTasks : List<MainTaskWithSubTasks> = emptyList(),
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
class TasksViewModel @Inject constructor(
    application : Application,
    private val tasksRepository : TasksRepository
) : ViewModel() {
    private val _tasksUiState = MutableStateFlow(TasksUiState())

    val tasksUiState : StateFlow<TasksUiState>
        get() = _tasksUiState

    private val notification = NotificationScheduler(application.applicationContext)

    init {
        viewModelScope.launch {
            combine(
                tasksRepository.getAllSubTasks(),
                tasksRepository.getAllMainTasks(),
                tasksRepository.getAllActiveMainTasksWithSubTasks()
            ) { tasks, taskGroups, taskGroupsWithSubTasks ->
                TasksUiState(
                    subTasks = tasks,
                    mainTasks = taskGroups,
                    mainTasksWithSubTasks = taskGroupsWithSubTasks
                )
            }
                .catch { throwable ->
                    throwable.printStackTrace()
                }
                .collect {
                    _tasksUiState.value = it
                }
        }

    }

    fun updateMainTaskWithSubTasks(mainTaskWithSubTasks : MainTaskWithSubTasks) {
        tasksRepository.updateMainTask(mainTaskWithSubTasks.mainTask)
        tasksRepository.updateSubTasks(mainTaskWithSubTasks.subTasks)
    }

    fun deleteMainTaskWithSubTasks(mainTaskWithSubTasks : MainTaskWithSubTasks) =
        tasksRepository
            .deleteMainTask(mainTaskWithSubTasks.mainTask)
            .invokeOnCompletion {
                notification.cancelNotification(mainTaskWithSubTasks.mainTask.mainTaskId.toInt())
                mainTaskWithSubTasks.subTasks.forEach {
                    notification.cancelNotification(it.subTaskId.toInt() * 1009)
                }
            }

    fun updateMainTask(mainTask : MainTask) {
        tasksRepository.updateMainTask(mainTask)

        if (mainTask.isArchived) {
            notification.cancelNotification(mainTask.mainTaskId.toInt())
        }
    }
}


