package com.je.playground.feature.tasks.editor

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.je.playground.database.tasks.entity.InvalidSubTaskException
import com.je.playground.database.tasks.entity.InvalidTaskException
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.entity.TaskWithSubTasks
import com.je.playground.feature.tasks.domain.GetTaskWithSubTasksByTaskIdUseCase
import com.je.playground.feature.tasks.domain.SaveTaskAndSubTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class TaskEditorViewModel @Inject constructor(
    savedStateHandle : SavedStateHandle,
    private val getTaskWithSubTasksByTaskIdUseCase : GetTaskWithSubTasksByTaskIdUseCase,
    private val saveTaskAndSubTasks : SaveTaskAndSubTasksUseCase
) : ViewModel() {
    sealed class State {
        data object Loading : State()
        data object Ready : State()
    }

    private val _taskEditorUiState : MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val taskEditorUiState = _taskEditorUiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _task : MutableStateFlow<Task> = MutableStateFlow(Task())
    val task : StateFlow<Task> = _task.asStateFlow()

    val subTasks = mutableStateListOf<SubTask>()
    private val removedSubTasks : MutableList<Long> = mutableListOf()

    private val mainTaskId : Long = checkNotNull(savedStateHandle.get<Long>("mainTaskId"))

    val isGroup : MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            getTaskWithSubTasksByTaskId(mainTaskId).let {
                it?.let {
                    _task.value = it.task
                    subTasks.addAll(it.subTasks)
                    isGroup.value = it.subTasks.isNotEmpty()
                }

                _taskEditorUiState.value = State.Ready
            }
        }
    }

    private suspend fun getTaskWithSubTasksByTaskId(taskId : Long) : TaskWithSubTasks? = withContext(Dispatchers.IO) { getTaskWithSubTasksByTaskIdUseCase(taskId) }

    fun onEvent(event : TaskEditorEvent) {
        when (event) {
            is TaskEditorEvent.SaveSubTask -> viewModelScope.launch {
                try {
                    if (event.subTask.title.isBlank()) {
                        throw InvalidSubTaskException("A subtask must have a title.")
                    }

                    if (event.index == -1) {
                        subTasks.add(event.subTask)
                    } else {
                        subTasks[event.index] = subTasks[event.index].copy(
                            title = event.subTask.title,
                            note = event.subTask.note,
                            startDate = event.subTask.startDate,
                            startTime = event.subTask.startTime,
                            endDate = event.subTask.endDate,
                            endTime = event.subTask.endTime
                        )
                    }

                    _eventFlow.emit(Event.Saved)
                } catch (e : InvalidSubTaskException) {
                    _eventFlow.emit(Event.ShowSnackbar(message = e.message ?: "Subtask couldn't be saved."))
                }
            }

            is TaskEditorEvent.RemoveSubTask -> removedSubTasks.add(subTasks.removeAt(event.index).subTaskId)
            is TaskEditorEvent.UpdateTask -> _task.update { current ->
                current.copy(
                    title = event.task.title,
                    note = event.task.note,
                    priority = event.task.priority,
                    startDate = event.task.startDate,
                    startTime = event.task.startTime,
                    endDate = event.task.endDate,
                    endTime = event.task.endTime
                )
            }

            is TaskEditorEvent.SaveTask -> viewModelScope.launch {
                try {
                    saveTaskAndSubTasks(
                        _task.value,
                        subTasks,
                        removedSubTasks
                    )

                    _eventFlow.emit(Event.Saved)
                } catch (e : InvalidTaskException) {
                    _eventFlow.emit(
                        Event.ShowSnackbar(
                            message = e.message ?: "Couldn't save task."
                        )
                    )
                }
            }

            is TaskEditorEvent.ToggleGroup -> toggleGroup()
        }
    }

    private fun toggleGroup() {
        viewModelScope.launch {
            if (isGroup.value) {
                if (subTasks.isNotEmpty()) {
                    _eventFlow.emit(Event.ShowSnackbar("You have to remove all subtasks first."))
                } else {
                    isGroup.value = !isGroup.value
                }
            } else {
                isGroup.value = !isGroup.value
            }
        }
    }

    sealed class Event {
        data class ShowSnackbar(val message : String) : Event()
        data object Saved : Event()
    }
}
