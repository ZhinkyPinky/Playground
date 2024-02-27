package com.je.playground.feature.tasks.editor

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.entity.TaskWithSubTasks
import com.je.playground.database.tasks.repository.TaskRepository
import com.je.playground.feature.tasks.domain.GetTaskWithSubTasksByTaskIdUseCase
import com.je.playground.notification.NotificationItem
import com.je.playground.notification.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject


@HiltViewModel
class TaskEditorViewModel @Inject constructor(
    application : Application,
    savedStateHandle : SavedStateHandle,
    private val taskRepository : TaskRepository,
    private val getTaskWithSubTasksByTaskIdUseCase : GetTaskWithSubTasksByTaskIdUseCase,
) : ViewModel() {
    sealed class State {
        data object Loading : State()
        data object Ready : State()
    }

    private val _taskEditorUiState : MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val taskEditorUiState : StateFlow<State> = _taskEditorUiState.asStateFlow()

    private val _Task : MutableStateFlow<Task> = MutableStateFlow(Task())
    val task : StateFlow<Task> = _Task.asStateFlow()

    val subTasks = mutableStateListOf<SubTask>()
    private val removedSubTasks : MutableList<Long> = mutableListOf()

    private val notification = NotificationScheduler(application.applicationContext)

    private val mainTaskId : Long = checkNotNull(savedStateHandle.get<Long>("mainTaskId"))

    init {
        viewModelScope.launch {
            getTaskWithSubTasksByTaskId(mainTaskId).let {
                it?.let {
                    _Task.value = it.task
                    subTasks.addAll(it.subTasks)
                }

                _taskEditorUiState.value = State.Ready
            }
        }
    }

    fun updateMainTask(task : Task) {
        _Task.update { current ->
            current.copy(
                title = task.title,
                note = task.note,
                priority = task.priority,
                startDate = task.startDate,
                startTime = task.startTime,
                endDate = task.endDate,
                endTime = task.endTime
            )
        }

        Log.d(
            "startDate",
            task.startDate.toString()
        )
    }

    private fun addSubTask(subTask : SubTask) {
        subTasks.add(subTask)
    }

    fun removeSubTask(index : Int) {
        removedSubTasks.add(subTasks.removeAt(index).subTaskId)
    }

    fun saveSubTask(
        index : Int,
        subTask : SubTask
    ) {
        if (index == -1) {
            addSubTask(subTask = subTask)
        } else {
            subTasks[index] = subTasks[index].copy(
                title = subTask.title,
                note = subTask.note,
                startDate = subTask.startDate,
                startTime = subTask.startTime,
                endDate = subTask.endDate,
                endTime = subTask.endTime
            )
        }
    }

    private suspend fun getTaskWithSubTasksByTaskId(taskId : Long) : TaskWithSubTasks? =
        withContext(Dispatchers.IO) { getTaskWithSubTasksByTaskIdUseCase(taskId) }

    fun saveMainTaskWithSubTasks() {
        viewModelScope.launch {
            if (_taskEditorUiState.value is State.Ready) {
                val mainTask = _Task.value
                val subTasks = subTasks

                mainTask.isCompleted = subTasks.isNotEmpty() && subTasks.all { it.isCompleted }

                mainTask.mainTaskId = taskRepository.insertTask(mainTask)
                subTasks.forEach { subTask ->
                    subTask.mainTaskId = mainTask.mainTaskId
                    subTask.subTaskId = taskRepository.insertSubTask(subTask)
                }

                if (mainTask.startDate != null) {
                    val startTime = mainTask.startTime ?: LocalTime.MIDNIGHT
                    notification.scheduleNotification(
                        NotificationItem(
                            id = mainTask.mainTaskId.toInt(),
                            title = mainTask.title,
                            message = mainTask.note,
                            dateTime = LocalDateTime.of(
                                mainTask.startDate,
                                startTime
                            )
                        )
                    )
                } else {
                    notification.cancelNotification(mainTask.mainTaskId.toInt())
                }

                subTasks.forEach { subTask ->
                    if (subTask.startDate != null) {
                        val startTime = subTask.startTime ?: LocalTime.MIDNIGHT
                        notification.scheduleNotification(
                            NotificationItem(
                                id = subTask.subTaskId.toInt() * 1009,
                                title = subTask.title,
                                message = subTask.note,
                                dateTime = LocalDateTime.of(
                                    subTask.startDate,
                                    startTime
                                )
                            )
                        )
                    } else {
                        notification.cancelNotification(subTask.subTaskId.toInt() * 1009)
                    }
                }

                removedSubTasks.forEach {
                    notification.cancelNotification(it.toInt() * 1009)
                }
            }
        }
    }
}