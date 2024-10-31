package com.je.playground.feature.tasks.editor.overview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.repository.SubTaskRepository
import com.je.playground.database.tasks.repository.TaskRepository
import com.je.playground.feature.tasks.editor.overview.TaskEditorOverviewEvent.RemoveSubTask
import com.je.playground.feature.tasks.editor.overview.TaskEditorOverviewEvent.UndoRemoveSubTask
import com.je.playground.feature.tasks.editor.overview.TaskEditorOverviewViewModel.State.Loading
import com.je.playground.feature.tasks.editor.overview.TaskEditorOverviewViewModel.State.Ready
import com.je.playground.feature.tasks.editor.overview.domain.DeleteSubTaskUseCase
import com.je.playground.feature.tasks.editor.subTask.domain.SaveSubTaskUseCase
import com.je.playground.feature.utility.Event
import com.je.playground.feature.utility.Result.Failure
import com.je.playground.feature.utility.Result.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskEditorOverviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    private val subTaskRepository: SubTaskRepository,
    private val saveSubTask: SaveSubTaskUseCase,
    private val deleteSubTask: DeleteSubTaskUseCase,
) : ViewModel() {
    sealed class State {
        data object Loading : State()
        data class Ready(
            val task: Task = Task(),
            val subTasks: List<SubTask> = emptyList(),
        ) : State()

        data object Saved : State()
    }

    private val taskId: Long = checkNotNull(savedStateHandle.get<Long>("taskId"))

    val taskEditorOverviewState = combine(
        taskRepository.getTaskFlowById(taskId),
        subTaskRepository.getAllByTaskId(taskId)
    ) { task, subTasks ->
        Ready(
            task = task,
            subTasks = subTasks
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Loading
    )

    private val _eventFlow = MutableSharedFlow<Event<String>>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val removedSubTasks: ArrayDeque<SubTask> = ArrayDeque()

    init {
        viewModelScope.launch {
            combine(
                taskRepository.getTaskFlowById(taskId),
                subTaskRepository.getAllByTaskId(taskId)
            ) { task, subTasks ->
                Ready(
                    task = task,
                    subTasks = subTasks
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = Loading
            )
            /*
            getTaskWithSubTasksByTaskId(taskId).collect { taskWithSubTasks ->
                when (_taskEditorOverviewState.value) {
                    is Loading -> _taskEditorOverviewState.value = Ready(
                        task = taskWithSubTasks.task,
                        subTasks = taskWithSubTasks.subTasks
                    )

                    is Ready -> _taskEditorOverviewState.update { currentState ->
                        if (currentState is Ready) {
                            currentState.copy(
                                task = taskWithSubTasks.task,
                                subTasks = taskWithSubTasks.subTasks
                            )
                        } else {
                            currentState
                        }
                    }

                    is Saved -> {}
                }
            }

             */
        }
    }

    fun onEvent(event: TaskEditorOverviewEvent) = when (event) {
        is RemoveSubTask -> removeSubTask(event.subTask)
        is UndoRemoveSubTask -> undoRemoveSubTask()
    }

    private fun removeSubTask(subTask: SubTask) = viewModelScope.launch {
        if (taskEditorOverviewState.value !is Ready) {
            showSnackBar("State: Not ready.")
            return@launch
        }

        if (deleteSubTask(subTask) == 1) {
            removedSubTasks.add(subTask)
            showSnackBar("Subtask deleted")
        } else {
            showSnackBar("The subtask could not be removed")
            return@launch
        }
    }

    private fun undoRemoveSubTask() = viewModelScope.launch {
        if (taskEditorOverviewState.value !is Ready) {
            showSnackBar("State: Not ready.")
            return@launch
        }

        val subTaskToRestore = removedSubTasks.lastOrNull()

        if (subTaskToRestore == null) {
            showSnackBar("There's no subtasks left to restore")
            return@launch
        }

        when (saveSubTask(subTaskToRestore)) {
            is Failure -> showSnackBar("Subtask could not be restored")
            is Success -> removedSubTasks.removeLast()
        }
    }

    private fun showSnackBar(message: String) = viewModelScope.launch {
        _eventFlow.emit(Event(message))
    }
}