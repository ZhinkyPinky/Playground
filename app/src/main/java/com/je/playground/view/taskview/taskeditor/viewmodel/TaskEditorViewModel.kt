package com.je.playground.view.taskview.taskeditor.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.je.playground.database.tasks.entity.MainTask
import com.je.playground.database.tasks.entity.MainTaskWithSubTasks
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.repository.TasksRepository
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
    private val tasksRepository : TasksRepository
) : ViewModel() {
    sealed class State {
        data object Loading : State()
        data object Ready : State()
    }

    private val _taskEditorUiState : MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val taskEditorUiState : StateFlow<State> = _taskEditorUiState.asStateFlow()

    private val _mainTask : MutableStateFlow<MainTask> = MutableStateFlow(MainTask())
    val mainTask : StateFlow<MainTask> = _mainTask.asStateFlow()

    val subTasks = mutableStateListOf<SubTask>()
    private val removedSubTasks : MutableList<Long> = mutableListOf()

    private val notification = NotificationScheduler(application.applicationContext)

    private val mainTaskId : Long = checkNotNull(savedStateHandle.get<Long>("mainTaskId"))

    init {
        viewModelScope.launch {
            selectMainTaskWithSubTasksByMainTaskId(mainTaskId).let {
                it?.let {
                    _mainTask.value = it.mainTask
                    subTasks.addAll(it.subTasks)
                }

                _taskEditorUiState.value = State.Ready
            }
        }
    }

    fun updateMainTask(mainTask : MainTask) {
        _mainTask.update { current ->
            current.copy(
                title = mainTask.title,
                note = mainTask.note,
                priority = mainTask.priority,
                startDate = mainTask.startDate,
                startTime = mainTask.startTime,
                endDate = mainTask.endDate,
                endTime = mainTask.endTime
            )
        }
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


    private suspend fun selectMainTaskWithSubTasksByMainTaskId(mainTaskId : Long) : MainTaskWithSubTasks? = withContext(Dispatchers.IO) { tasksRepository.getMainTaskWithSubTasksByMainTaskId(mainTaskId) }

    fun saveMainTaskWithSubTasks() {
        viewModelScope.launch {
            if (_taskEditorUiState.value is State.Ready) {
                val mainTask = _mainTask.value
                val subTasks = subTasks

                mainTask.isCompleted = subTasks.isNotEmpty() && subTasks.all { it.isCompleted }

                mainTask.mainTaskId = tasksRepository.insertMainTask(mainTask)
                subTasks.forEach { subTask ->
                    subTask.mainTaskId = mainTask.mainTaskId
                    subTask.subTaskId = tasksRepository.insertSubTask(subTask)
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