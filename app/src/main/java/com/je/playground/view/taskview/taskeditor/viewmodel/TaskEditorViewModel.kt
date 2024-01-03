package com.je.playground.view.taskview.taskeditor.viewmodel

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.je.playground.database.tasks.entity.MainTask
import com.je.playground.database.tasks.entity.MainTaskWithSubTasks
import com.je.playground.database.tasks.repository.TasksRepository
import com.je.playground.view.Notifications
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        data class Data(
            val mainTaskWithSubTasks : MainTaskWithSubTasks = MainTaskWithSubTasks(
                mainTask = MainTask(),
                subTasks = emptyList()
            )
        ) : State()
    }

    private val notifications = Notifications(application)

    private val _taskEditorUiState = MutableStateFlow<State>(State.Loading)
    val taskEditorUiState = _taskEditorUiState.asStateFlow()
    val mainTaskId : Long = checkNotNull(savedStateHandle.get<Long>("mainTaskId"))

    init {
        viewModelScope.launch {
            selectMainTaskWithSubTasksByMainTaskId(mainTaskId).let {
                _taskEditorUiState.value = if (it == null) State.Data() else State.Data(it)
            }
        }
    }

    suspend fun selectMainTaskWithSubTasksByMainTaskId(mainTaskId : Long) : MainTaskWithSubTasks? = withContext(Dispatchers.IO) { tasksRepository.getMainTaskWithSubTasksByMainTaskId(mainTaskId) }

    fun saveMainTaskWithSubTasks(mainTaskWithSubTasks : MainTaskWithSubTasks) {
        viewModelScope.launch {
            val mainTask = mainTaskWithSubTasks.mainTask
            val subTasks = mainTaskWithSubTasks.subTasks

            val mainTaskId = tasksRepository.insertMainTask(mainTask)
            subTasks.forEach { subTask ->
                subTask.mainTaskId = mainTaskId
                subTask.subTaskId = tasksRepository.insertSubTask(subTask)
            }

            mainTask.startDate?.let { startDate ->
                val startTime = mainTask.startTime ?: LocalTime.MIDNIGHT
                notifications.scheduleNotification(
                    mainTask.mainTaskId,
                    mainTask.title,
                    LocalDateTime.of(
                        startDate,
                        startTime
                    )
                )
            }

            subTasks.forEach { subTask ->
                subTask.startDate?.let { startDate ->
                    val startTime = subTask.startTime ?: LocalTime.MIDNIGHT
                    notifications.scheduleNotification(
                        subTask.subTaskId,
                        subTask.title,
                        LocalDateTime.of(
                            startDate,
                            startTime
                        )
                    )
                }
            }
        }
    }


    fun updateMainTaskWithSubTasks(mainTaskWithSubTasks : MainTaskWithSubTasks) {
        TODO("Not yet implemented")
    }

    fun deleteMainTaskWithSubTasks(mainTaskWithSubTasks : MainTaskWithSubTasks) {
        TODO("Not yet implemented")
    }


    companion object {
        private const val ERROR_INSERT_FAILED : Long = -1
    }
}