package com.je.playground.feature.tasks.editor.subTask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.feature.tasks.editor.subTask.SubTaskEditorViewModel.State.*
import com.je.playground.feature.tasks.editor.subTask.domain.GetSubTaskUseCase
import com.je.playground.feature.tasks.editor.subTask.domain.SaveSubTaskUseCase
import com.je.playground.feature.utility.Event
import com.je.playground.feature.utility.Result.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubTaskEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSubTask: GetSubTaskUseCase,
    private val saveSubTask: SaveSubTaskUseCase
) : ViewModel() {
    sealed interface State {
        data object Loading : State
        data class Ready(
            val subTask: SubTask,
        ) : State

        data object Saved : State
    }

    private val taskId: Long = checkNotNull(savedStateHandle.get<Long>("taskId"))
    private val subTaskId: Long = checkNotNull(savedStateHandle.get<Long>("subTaskId"))

    private val _subTaskEditorState: MutableStateFlow<State> = MutableStateFlow(Loading)
    val subTaskEditorState = _subTaskEditorState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<Event<String>>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            _subTaskEditorState.update {
                Ready(subTask = getSubTask(subTaskId).copy(taskId = taskId))
            }
        }
    }

    fun onEvent(event: SubTaskEditorEvent): Unit {
        when (event) {
            is SubTaskEditorEvent.Update -> update(event.fields)
            is SubTaskEditorEvent.Save -> save()
        }
    }

    private fun update(fields: List<SubTaskField>) = _subTaskEditorState.update { currentState ->
        if (currentState !is Ready) {
            showSnackBar("State: Not ready")
            return
        }

        val modifiedSubTask = currentState.subTask.copy().apply {
            fields.forEach { field ->
                when (field) {
                    is SubTaskField.Completed -> isCompleted = field.completed
                    is SubTaskField.EndDate -> endDate = field.endDate
                    is SubTaskField.EndTime -> endTime = field.endTime
                    is SubTaskField.Note -> note = field.note
                    is SubTaskField.StartDate -> startDate = field.startDate
                    is SubTaskField.StartTime -> startTime = field.startTime
                    is SubTaskField.Title -> {
                        if (field.title.isBlank()) {
                            showSnackBar("Title required")
                            return
                        }

                        title = field.title
                    }
                }
            }
        }

        currentState.copy(subTask = modifiedSubTask)
    }

    private fun save() = viewModelScope.launch {
        _subTaskEditorState.value.let { currentState ->
            if (currentState is Ready) {
                when (val result = saveSubTask(currentState.subTask)) {
                    is Failure -> showSnackBar(result.message)
                    is Success -> _subTaskEditorState.update { Saved }
                }
            }
        }
    }

    private fun showSnackBar(message: String) = viewModelScope.launch {
        _eventFlow.emit(Event(message))
    }
}