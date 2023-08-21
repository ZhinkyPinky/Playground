package com.je.playground.ui.taskview.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.entity.TaskGroup
import com.je.playground.database.tasks.entity.TaskGroupWithTasks
import com.je.playground.database.tasks.repository.TasksRepository
import com.je.playground.ui.Notifications
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

data class TasksUiState(
    val tasks : List<Task> = emptyList(),
    val taskGroups : List<TaskGroup> = emptyList(),
    val taskGroupsWithTasks : List<TaskGroupWithTasks> = emptyList(),
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


enum class Priority {
    High,
    Medium,
    Low
}

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val application : Application,
    private val tasksRepository : TasksRepository
) : ViewModel() {


    private val notifications = Notifications(application)

    private val priorities = MutableStateFlow(
        Priority
            .values()
            .asList()
    )

    private val _tasksUiState = MutableStateFlow(TasksUiState())

    val tasksUiState : StateFlow<TasksUiState>
        get() = _tasksUiState

    init {
        viewModelScope.launch {
            combine(
                tasksRepository.getAllTasks(),
                tasksRepository.getAllTaskGroups(),
                tasksRepository.getAllTaskGroupsWithTasks()
            ) { tasks, taskGroups, taskGroupsWithTasks ->
                TasksUiState(
                    tasks = tasks,
                    taskGroups = taskGroups,
                    taskGroupsWithTasks = taskGroupsWithTasks
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

    /*
    //region Exercise

    fun insertExercise(exercise : Exercise) = tasksRepository.insertExercise(exercise)

    fun updateExercise(exercise : Exercise) = tasksRepository.updateExercise(exercise)

    fun deleteExercise(exercise : Exercise) = tasksRepository.deleteExercise(exercise)

    //endregion

    //region ExerciseProgram

    fun insertExerciseProgram(name : String) = viewModelScope.launch {
        val exerciseProgram = ExerciseProgram(
            id = insertTask(),
            name = name
        )

        tasksRepository.insertExerciseProgram(exerciseProgram)
    }


    fun updateExerciseProgram(exerciseProgram : ExerciseProgram) = tasksRepository.updateExerciseProgram(exerciseProgram)
    //endregion

    //region ExerciseOccasion

    fun insertExerciseOccasion(exerciseOccasion : ExerciseOccasion) = tasksRepository.insertExerciseOccasion(exerciseOccasion)

    fun updateExerciseOccasion(exerciseOccasion : ExerciseOccasion) = viewModelScope.launch {
        tasksRepository.updateExerciseOccasion(exerciseOccasion)
    }

    fun deleteExerciseOccasion(exerciseOccasion : ExerciseOccasion) = tasksRepository.deleteExerciseOccasion(exerciseOccasion)

    //endregion

    //region SimpleTask

    fun updateSimpleTask(simpleTask : SimpleTask) = tasksRepository.updateSimpleTask(simpleTask)

    //endregion

    //region Task

    private suspend fun insertTask() : Long = withContext(viewModelScope.coroutineContext) { tasksRepository.insertTask() }

    fun deleteTask(task : Task) = viewModelScope.launch {
        notifications.cancelNotification(task.id)
        tasksRepository.deleteTask(task)
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
        tasksRepository.insertTaskOccasion(
            TaskOccasion(
                taskId = taskId,
                dateFrom = dateFrom,
                timeFrom = timeFrom,
                dateTo = dateTo,
                timeTo = timeTo
            )
        )
    }

    fun updateTaskOccasion(taskOccasion : TaskOccasion) = tasksRepository.updateTaskOccasion(taskOccasion)

    fun deleteTaskOccasion(taskOccasion : TaskOccasion) = tasksRepository.deleteTaskOccasion(taskOccasion)

    private fun deleteTaskOccasionWithTaskIdAndDateTimeFrom(taskOccasion : TaskOccasion) = tasksRepository.deleteTaskOccasionWithTaskIdAndDateFrom(taskOccasion)

    //endregion

    //region WeekdaySchedule

    fun insertWeekdayScheduleEntry(weekdayScheduleEntry : WeekdaySchedule) {
        tasksRepository.insertWeekdayScheduleEntry(weekdayScheduleEntry)

        val currentDate = LocalDate.now()

        for (i in 0..30L) {
            val date = LocalDate
                .of(
                    currentDate.year,
                    currentDate.month,
                    currentDate.dayOfMonth
                )
                .plusDays(i)

            if (date.dayOfWeek == weekdayScheduleEntry.weekday) {
                insertTaskOccasion(
                    taskId = weekdayScheduleEntry.id,
                    dateFrom = date,
                )
            }
        }
    }

    fun deleteWeekdayScheduleEntry(weekdayScheduleEntry : WeekdaySchedule) {
        tasksRepository.deleteWeekdayScheduleEntry(weekdayScheduleEntry)

        val currentDate = LocalDate.now()

        for (i in 0..30L) {
            val date = LocalDate
                .of(
                    currentDate.year,
                    currentDate.month,
                    currentDate.dayOfMonth,
                )
                .plusDays(i)

            if (date.dayOfWeek == weekdayScheduleEntry.weekday) {
                deleteTaskOccasionWithTaskIdAndDateTimeFrom(
                    TaskOccasion(
                        taskId = weekdayScheduleEntry.id,
                        dateFrom = date
                    )
                )
            }
        }
    }

    //endregion
     */


    fun insertTaskGroupWithTasks(taskGroupWithTasks : TaskGroupWithTasks) {
        viewModelScope.launch {
            val taskGroup = taskGroupWithTasks.taskGroup
            val tasks = taskGroupWithTasks.tasks

            val taskGroupId = tasksRepository.insertTaskGroup(taskGroup)
            if (taskGroupId == -1L) {
                tasks.forEach { task ->
                    task.taskGroupId = taskGroupId
                    task.taskId = tasksRepository.insertTask(task)
                }

            }

            taskGroup.startDate?.let { startDate ->
                val startTime = taskGroup.startTime ?: LocalTime.MIDNIGHT
                notifications.scheduleNotification(
                    taskGroup.taskGroupId,
                    taskGroup.title,
                    LocalDateTime.of(
                        startDate,
                        startTime
                    )
                )
            }
        }
    }

    fun updateTaskGroupWithTasks(taskGroupWithTasks : TaskGroupWithTasks) {
        TODO("Not yet implemented")
    }

    fun deleteTaskGroupWithTasks(taskGroupWithTasks : TaskGroupWithTasks) {
        TODO("Not yet implemented")
    }


    companion object {
        private const val ERROR_INSERT_FAILED : Long = -1
    }
}


