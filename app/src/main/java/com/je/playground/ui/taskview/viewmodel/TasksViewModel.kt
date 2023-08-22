package com.je.playground.ui.taskview.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.je.playground.database.tasks.entity.MainTask
import com.je.playground.database.tasks.entity.MainTaskWithSubTasks
import com.je.playground.database.tasks.entity.SubTask
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
    val subTasks : List<SubTask> = emptyList(),
    val mainTasks : List<MainTask> = emptyList(),
    val taskGroupsWithSubTasks : List<MainTaskWithSubTasks> = emptyList(),
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
    Low,
    Medium,
    High
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
                tasksRepository.getAllSubTasks(),
                tasksRepository.getAllMainTasks(),
                tasksRepository.getAllMainTasksWithSubTasks()
            ) { tasks, taskGroups, taskGroupsWithSubTasks ->
                TasksUiState(
                    subTasks = tasks,
                    mainTasks = taskGroups,
                    taskGroupsWithSubTasks = taskGroupsWithSubTasks
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


