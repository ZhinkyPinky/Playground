package com.je.playground.ui.tasklist.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.je.playground.databaseV2.repository.TasksRepositoryV2
import com.je.playground.databaseV2.tasks.entity.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

data class TasksUiStateV2(
    val simpleTasks : List<SimpleTask> = emptyList(),
    val exerciseProgramsWithExercises : List<ExerciseProgramWithExercises> = emptyList(),
    val tasks : List<Task> = emptyList(),
    val taskOccasions : List<TaskOccasion> = emptyList(),
    val tasksWithOccasions : List<TaskWithOccasions> = emptyList(),
    val priorities : List<Priority> = Priority
        .values()
        .asList()
)

enum class TaskType(type : String) {
    SimpleTask("Basic Task"),
    ExerciseProgram("Exercise Program")
}

enum class Priority {
    High,
    Medium,
    Low
}
@HiltViewModel
class TasksViewModelV2 @Inject constructor(
    private val application: Application,
    private val tasksRepositoryV2 : TasksRepositoryV2
) : ViewModel() {
    private val notifications = Notifications(application)

    private val priorities = MutableStateFlow(
        Priority
            .values()
            .asList()
    )

    private val _tasksUiStateV2 = MutableStateFlow(TasksUiStateV2())

    val tasksUiState : StateFlow<TasksUiStateV2>
        get() = _tasksUiStateV2

    init {
        viewModelScope.launch {
            combine(
                tasksRepositoryV2.getAllSimpleTask(),
                tasksRepositoryV2.getAllExerciseProgramsWithExercises(),
                tasksRepositoryV2.getAllTasks(),
                tasksRepositoryV2.getAllTaskOccasions(),
                tasksRepositoryV2.getAllTasksWithOccasions()
            ) { simpleTasks, exerciseProgramWithExercises, tasks, taskOccasions, tasksWithOccasions ->
                TasksUiStateV2(
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

    //region ExerciseOccasion

    fun insertExerciseOccasion(exerciseOccasion : ExerciseOccasion) = tasksRepositoryV2.insertExerciseOccasion(exerciseOccasion)

    fun updateExerciseOccasion(exerciseOccasion : ExerciseOccasion) = viewModelScope.launch {
        tasksRepositoryV2.updateExerciseOccasion(exerciseOccasion)
    }

    fun deleteExerciseOccasion(exerciseOccasion : ExerciseOccasion) = tasksRepositoryV2.deleteExerciseOccasion(exerciseOccasion)

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
        when(val id = insertTask()){
            ERROR_INSERT_FAILED -> {}
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
                        taskName =  name,
                        notificationDateTime = if (timeFrom != null) LocalDateTime.of(dateFrom, timeFrom) else LocalDateTime.of(dateFrom, LocalTime.MIDNIGHT)
                        )
                }
            }
        }
    }

    fun updateSimpleTask(simpleTask : SimpleTask) = tasksRepositoryV2.updateSimpleTask(simpleTask)

    //endregion

    //region Task

    private suspend fun insertTask() : Long = withContext(viewModelScope.coroutineContext) { tasksRepositoryV2.insertTask() }

    fun deleteTask(task : Task) = viewModelScope.launch {
        notifications.cancelNotification(task.id)
        tasksRepositoryV2.deleteTask(task)  }



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

    //region WeekdaySchedule

    fun insertWeekdayScheduleEntry(weekdayScheduleEntry : WeekdaySchedule) {
        tasksRepositoryV2.insertWeekdayScheduleEntry(weekdayScheduleEntry)

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
        tasksRepositoryV2.deleteWeekdayScheduleEntry(weekdayScheduleEntry)

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


    companion object {
        private const val ERROR_INSERT_FAILED : Long = -1
        /*
        fun provideFactory(
            tasksRepositoryV2 : TasksRepositoryV2 = GraphV2.tasksRepositoryV2,
            application : PlaygroundApplication,
            owner : SavedStateRegistryOwner,
            defaultArgs : Bundle? = null,
        ) : AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory(
                owner,
                defaultArgs
            ) {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key : String,
                    modelClass : Class<T>,
                    handle : SavedStateHandle
                ) : T {
                    return TasksViewModelV2(
                        application = application,
                        tasksRepositoryV2 = tasksRepositoryV2
                    ) as T
                }
            }
         */
    }
}

/*
class TestWorker(
    context : Context,
    workerParameters : WorkerParameters
) : Worker(
    context,
    workerParameters
) {
    override fun doWork(): Result {
        // Perform the task here

        // Return Result.success() if the task was successful, or Result.failure() if it failed
        return Result.success()
    }

// Enqueue the worker to run every 15 minutes
val workRequest = PeriodicWorkRequestBuilder<TestWorker>(15, TimeUnit.MINUTES).build()
WorkManager.getInstance(application).enqueue(workRequest)

}
 */
