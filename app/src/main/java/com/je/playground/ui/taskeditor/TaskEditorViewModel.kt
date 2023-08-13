package com.je.playground.ui.taskeditor

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.je.playground.databaseV2.tasks.entity.Exercise
import com.je.playground.databaseV2.tasks.entity.ExerciseProgram
import com.je.playground.databaseV2.tasks.entity.SimpleTask
import com.je.playground.databaseV2.tasks.entity.Task
import com.je.playground.databaseV2.tasks.entity.TaskOccasion
import com.je.playground.databaseV2.tasks.repository.TasksRepositoryV2
import com.je.playground.ui.tasklist.viewmodel.Notifications
import com.je.playground.ui.tasklist.viewmodel.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

enum class TaskType(type : String) {
    SimpleTask("Basic Task"),
    ExerciseProgram("Exercise Program")
}


data class TaskEditorUiState(
    val priorities : List<Priority> = Priority
        .values()
        .asList()
)

@HiltViewModel
class TaskEditorViewModel @Inject constructor(
    private val application : Application,
    private val tasksRepositoryV2 : TasksRepositoryV2
) : ViewModel() {
    private val notifications = Notifications(application)

    private val _taskEditorUiState = MutableStateFlow(TaskEditorUiState())
    val taskEditorUiState : StateFlow<TaskEditorUiState>
        get() = _taskEditorUiState

    /*
    init {
        viewModelScope.launch {
            combine(
                tasksRepositoryV2.getAllSimpleTask(),
                tasksRepositoryV2.getAllExerciseProgramsWithExercises(),
                tasksRepositoryV2.getAllTasks(),
                tasksRepositoryV2.getAllTaskOccasions(),
                tasksRepositoryV2.getAllTasksWithOccasions()
            ) { simpleTasks, exerciseProgramWithExercises, tasks, taskOccasions, tasksWithOccasions ->
                TaskEditorUiState(
                    simpleTasks = simpleTasks,
                    exerciseProgramsWithExercises = exerciseProgramWithExercises,
                    tasks = tasks,
                    taskOccasions = taskOccasions,
                    tasksWithOccasions = tasksWithOccasions
                )
            }
                .catch { throwable ->
                    throwable.printStackTrace()
                }
                .collect {
                    _tasksUiStateV2.value = it
                }
        }
    }
     */

    //region Task
    private suspend fun insertTask() : Long = withContext(viewModelScope.coroutineContext) { tasksRepositoryV2.insertTask() }

    fun deleteTask(task : Task) = viewModelScope.launch {
        notifications.cancelNotification(task.id)
        tasksRepositoryV2.deleteTask(task)
    }
    //endregion

    //region TaskOccasion
    fun insertTaskOccasion(
        taskId : Long,
        dateFrom : LocalDate? = null,
        timeFrom : LocalTime? = null,
        dateTo : LocalDate? = null,
        timeTo : LocalTime? = null
    ) {
        tasksRepositoryV2.insertTaskOccasion(
            TaskOccasion(
                taskId = taskId,
                dateFrom = dateFrom,
                timeFrom = timeFrom,
                dateTo = dateTo,
                timeTo = timeTo
            )
        )
    }

    fun updateTaskOccasion(taskOccasion : TaskOccasion) = tasksRepositoryV2.updateTaskOccasion(taskOccasion)

    fun deleteTaskOccasion(taskOccasion : TaskOccasion) = tasksRepositoryV2.deleteTaskOccasion(taskOccasion)

    private fun deleteTaskOccasionWithTaskIdAndDateTimeFrom(taskOccasion : TaskOccasion) = tasksRepositoryV2.deleteTaskOccasionWithTaskIdAndDateFrom(taskOccasion)
    //endregion

    //region SimpleTask
    fun insertSimpleTask(
        name : String,
        priority : Priority,
        note : String?,
        dateFrom : LocalDate?,
        timeFrom : LocalTime?,
        dateTo : LocalDate?,
        timeTo : LocalTime?
    ) = viewModelScope.launch {
        when (val id = insertTask()) {
            ERROR_INSERT_FAILED -> {
                //TODO: Add error message?
            }

            else -> {
                val simpleTask = SimpleTask(
                    id = id,
                    name = name,
                    priority = priority.ordinal,
                    note = note
                )

                insertTaskOccasion(
                    taskId = simpleTask.id,
                    dateFrom = dateFrom,
                    timeFrom = timeFrom,
                    dateTo = dateTo,
                    timeTo = timeTo
                )

                tasksRepositoryV2.insertSimpleTask(simpleTask)

                if (dateFrom != null) {
                    notifications.scheduleNotification(
                        taskId = id,
                        taskName = name,
                        notificationDateTime = if (timeFrom != null) LocalDateTime.of(
                            dateFrom,
                            timeFrom
                        ) else LocalDateTime.of(
                            dateFrom,
                            LocalTime.MIDNIGHT
                        )
                    )
                }
            }
        }
    }

    fun updateSimpleTask(simpleTask : SimpleTask) = tasksRepositoryV2.updateSimpleTask(simpleTask)
    //endregion

    //region Exercise

    fun insertExercise(exercise : Exercise) = tasksRepositoryV2.insertExercise(exercise)

    fun updateExercise(exercise : Exercise) = tasksRepositoryV2.updateExercise(exercise)

    fun deleteExercise(exercise : Exercise) = tasksRepositoryV2.deleteExercise(exercise)

    //endregion

    //region ExerciseProgram

    fun insertExerciseProgram(name : String) = viewModelScope.launch {
        val exerciseProgram = ExerciseProgram(
            id = insertTask(),
            name = name
        )

        tasksRepositoryV2.insertExerciseProgram(exerciseProgram)
    }


    fun updateExerciseProgram(exerciseProgram : ExerciseProgram) = tasksRepositoryV2.updateExerciseProgram(exerciseProgram)
    //endregion

    companion object {
        private const val ERROR_INSERT_FAILED : Long = -1
    }
}

