package com.je.playground.feature.tasks.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.je.playground.database.tasks.entity.InvalidTaskException
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.entity.Task
import com.je.playground.feature.tasks.domain.GetTaskWithSubTasksByTaskIdUseCase
import com.je.playground.feature.tasks.domain.SaveTaskAndSubTasksUseCase
import com.je.playground.feature.utility.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTaskWithSubTasksByTaskId: GetTaskWithSubTasksByTaskIdUseCase,
    private val saveTaskAndSubTasks: SaveTaskAndSubTasksUseCase
) : ViewModel() {
    sealed class State {
        data object Loading : State()
        data class Ready(
            val task: Task = Task(),
            val subTasks: List<SubTask> = emptyList(),
        ) : State()

        data object Saved : State()
    }

    private val mainTaskId: Long = checkNotNull(savedStateHandle.get<Long>("taskId"))

    private val _taskEditorUiState: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val taskEditorUiState = _taskEditorUiState.asStateFlow()

    private val _snackbarFlow = MutableSharedFlow<Event<String>>()
    val snackbarFlow = _snackbarFlow.asSharedFlow()

    private val removedSubTasks: MutableList<Long> = mutableListOf()

    init {
        viewModelScope.launch {
            getTaskWithSubTasksByTaskId(mainTaskId).let {
                it?.let {
                    _taskEditorUiState.value = State.Ready(
                        task = it.task,
                        subTasks = it.subTasks,
                    )
                }
            }
        }
    }

    fun onEvent(event: TaskEditorEvent) {
        when (event) {
            is TaskEditorEvent.UpdateTask -> updateTask(event.fields)
            is TaskEditorEvent.UpdateSubTask -> updateSubTask(event.index, event.fields)
            is TaskEditorEvent.RemoveSubTask -> removeSubTask(event.index)
            is TaskEditorEvent.Save -> save()
        }
    }


    private fun updateTask(fields: List<TaskField>) = _taskEditorUiState.update { currentState ->
        if (currentState !is State.Ready) return@update currentState

        var task = currentState.task

        fields.forEach { field ->
            task = when (field) {
                is TaskField.Archived -> task.copy(isArchived = field.archived)
                is TaskField.Completed -> task.copy(isCompleted = field.completed)
                is TaskField.StartDate -> field.startDate?.let { task.copy(startDate = it) } ?: task
                is TaskField.EndDate -> field.endDate?.let { task.copy(endDate = it) } ?: task
                is TaskField.Note -> task.copy(note = field.note)
                is TaskField.Priority -> task.copy(priority = field.priority)
                is TaskField.StartTime -> task.copy(startTime = field.startTime)
                is TaskField.EndTime -> task.copy(endTime = field.endTime)
                is TaskField.Title -> task.copy(title = field.title)
            }
        }

        currentState.copy(task = task)
    }

    private fun updateSubTask(
        index: Int,
        fields: List<SubTaskField>
    ) = _taskEditorUiState.update { currentState ->
        if (currentState !is State.Ready) return@update currentState

        val subTasks = currentState.subTasks.toMutableList()
        var subTask = if (index != -1) subTasks[index] else SubTask()

        fields.forEach { field ->
            subTask = when (field) {
                is SubTaskField.Completed -> subTask.copy(isCompleted = field.completed)
                is SubTaskField.EndDate -> subTask.copy(endDate = field.endDate)
                is SubTaskField.EndTime -> subTask.copy(endTime = field.endTime)
                is SubTaskField.Note -> subTask.copy(note = field.note)
                is SubTaskField.StartDate -> subTask.copy(startDate = field.startDate)
                is SubTaskField.StartTime -> subTask.copy(startTime = field.startTime)
                is SubTaskField.Title -> subTask.copy(title = field.title)
            }
        }

        if (index != -1) subTasks[index] = subTask else subTasks.add(subTask)

        currentState.copy(subTasks = subTasks)
    }

    private fun removeSubTask(index: Int) = _taskEditorUiState.update { currentState ->
        if (currentState !is State.Ready) return@update currentState

        val subTasks = currentState.subTasks.toMutableList()
        val subTaskId = subTasks.removeAt(index).subTaskId

        removedSubTasks.add(subTaskId)

        currentState.copy(subTasks = subTasks)
    }

    private fun save() = viewModelScope.launch {
        try {
            _taskEditorUiState.value.let {
                if (it is State.Ready) {
                    saveTaskAndSubTasks(
                        it.task, it.subTasks, removedSubTasks
                    )
                    _taskEditorUiState.update { State.Saved }
                }
            }
        } catch (e: InvalidTaskException) {
            e.message?.let { showSnackBar(it) }
        }
    }

    private fun showSnackBar(message: String) = viewModelScope.launch {
        _snackbarFlow.emit(Event(message))
    }
}