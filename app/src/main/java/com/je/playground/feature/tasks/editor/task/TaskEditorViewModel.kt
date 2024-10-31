package com.je.playground.feature.tasks.editor.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.je.playground.database.tasks.entity.Task
import com.je.playground.feature.tasks.domain.GetTaskUseCase
import com.je.playground.feature.tasks.domain.SaveTaskUseCase
import com.je.playground.feature.tasks.editor.task.TaskEditorEvent.SaveTask
import com.je.playground.feature.tasks.editor.task.TaskEditorEvent.UpdateTask
import com.je.playground.feature.tasks.editor.task.TaskEditorViewModel.State.Loading
import com.je.playground.feature.tasks.editor.task.TaskEditorViewModel.State.Ready
import com.je.playground.feature.tasks.editor.task.TaskEditorViewModel.State.Saved
import com.je.playground.feature.tasks.editor.task.TaskField.Archived
import com.je.playground.feature.tasks.editor.task.TaskField.Completed
import com.je.playground.feature.tasks.editor.task.TaskField.EndDate
import com.je.playground.feature.tasks.editor.task.TaskField.EndTime
import com.je.playground.feature.tasks.editor.task.TaskField.Note
import com.je.playground.feature.tasks.editor.task.TaskField.Priority
import com.je.playground.feature.tasks.editor.task.TaskField.StartDate
import com.je.playground.feature.tasks.editor.task.TaskField.StartTime
import com.je.playground.feature.tasks.editor.task.TaskField.Title
import com.je.playground.feature.utility.Event
import com.je.playground.feature.utility.Result.Failure
import com.je.playground.feature.utility.Result.Success
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
    private val getTask: GetTaskUseCase,
    private val saveTask: SaveTaskUseCase
) : ViewModel() {
    sealed interface State {
        data object Loading : State
        data class Ready(val task: Task) : State
        data class Saved(val taskId: Long) : State
    }

    private val taskId: Long = checkNotNull(savedStateHandle.get<Long>("taskId"))

    private val _taskEditorState: MutableStateFlow<State> = MutableStateFlow(Loading)
    val taskEditorState = _taskEditorState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<Event<String>>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            _taskEditorState.update {
                Ready(task = getTask(taskId))
            }
        }
    }


    fun onEvent(event: TaskEditorEvent) {
        when (event) {
            is UpdateTask -> updateTask(event.fields)
            is SaveTask -> save()
        }
    }

    private fun updateTask(fields: List<TaskField>) = _taskEditorState.update { currentState ->
        if (currentState !is Ready) {
            showSnackBar("State: Not ready.")
            return
        }

        val modifiedTask = currentState.task.copy().apply {
            fields.forEach { field ->
                when (field) {
                    is Completed -> isCompleted = field.completed
                    is EndDate -> endDate = field.endDate
                    is EndTime -> endTime = field.endTime
                    is Note -> note = field.note
                    is StartDate -> startDate = field.startDate
                    is StartTime -> startTime = field.startTime
                    is Title -> {
                        if (field.title.isBlank()) {
                            showSnackBar("Title required")
                            return
                        }

                        title = field.title
                    }

                    is Archived -> TODO()
                    is Priority -> TODO()
                }
            }
        }

        currentState.copy(task = modifiedTask)
    }

    private fun save() = viewModelScope.launch {
        _taskEditorState.value.let { currentState ->
            if (currentState is Ready) {
                val result = saveTask(currentState.task)
                when (result.first) {
                    is Failure -> showSnackBar((result.first as Failure).message)
                    is Success -> _taskEditorState.update { Saved(result.second) }
                }
            }
        }
    }

    private fun showSnackBar(message: String) = viewModelScope.launch {
        _eventFlow.emit(Event(message))
    }
}
