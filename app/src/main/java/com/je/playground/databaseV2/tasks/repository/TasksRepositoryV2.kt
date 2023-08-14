package com.je.playground.databaseV2.tasks.repository

import com.je.playground.databaseV2.tasks.dao.ExerciseDao
import com.je.playground.databaseV2.tasks.dao.ExerciseOccasionDao
import com.je.playground.databaseV2.tasks.dao.ExerciseProgramDao
import com.je.playground.databaseV2.tasks.dao.ExerciseProgramWithExercisesDao
import com.je.playground.databaseV2.tasks.dao.SimpleTaskDao
import com.je.playground.databaseV2.tasks.dao.TaskDao
import com.je.playground.databaseV2.tasks.dao.TaskGroupDao
import com.je.playground.databaseV2.tasks.dao.TaskOccasionDao
import com.je.playground.databaseV2.tasks.dao.TaskV2Dao
import com.je.playground.databaseV2.tasks.dao.TaskWithOccasionsDao
import com.je.playground.databaseV2.tasks.dao.WeekdayScheduleDao
import com.je.playground.databaseV2.tasks.entity.Exercise
import com.je.playground.databaseV2.tasks.entity.ExerciseOccasion
import com.je.playground.databaseV2.tasks.entity.ExerciseProgram
import com.je.playground.databaseV2.tasks.entity.ExerciseProgramWithExercises
import com.je.playground.databaseV2.tasks.entity.SimpleTask
import com.je.playground.databaseV2.tasks.entity.Task
import com.je.playground.databaseV2.tasks.entity.TaskOccasion
import com.je.playground.databaseV2.tasks.entity.TaskWithOccasions
import com.je.playground.databaseV2.tasks.entity.WeekdaySchedule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class TasksRepositoryV2 @Inject constructor(
    private val exerciseDao : ExerciseDao,
    private val exerciseOccasionDao : ExerciseOccasionDao,
    private val exerciseProgramDao : ExerciseProgramDao,
    private val exerciseProgramWithExercisesDao : ExerciseProgramWithExercisesDao,
    private val simpleTaskDao : SimpleTaskDao,
    private val taskDao : TaskDao,
    private val taskOccasionDao : TaskOccasionDao,
    private val taskWithOccasionsDao : TaskWithOccasionsDao,
    private val weekdayScheduleDao : WeekdayScheduleDao,
    private val taskGroupDao : TaskGroupDao,
    private val taskV2Dao : TaskV2Dao
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    //region Exercise

    fun insertExercise(exercise : Exercise) = coroutineScope.launch {
        exerciseDao.insertExercise(exercise)
    }

    fun updateExercise(exercise : Exercise) = coroutineScope.launch {
        exerciseDao.updateExercise(exercise)
    }

    fun deleteExercise(exercise : Exercise) = coroutineScope.launch {
        exerciseDao.delete(exercise)
    }

    //endregion

    //region ExerciseOccasion

    fun insertExerciseOccasion(exerciseOccasion : ExerciseOccasion) = coroutineScope.launch { exerciseOccasionDao.insert(exerciseOccasion) }

    fun updateExerciseOccasion(exerciseOccasion : ExerciseOccasion) = coroutineScope.launch { exerciseOccasionDao.update(exerciseOccasion) }

    fun deleteExerciseOccasion(exerciseOccasion : ExerciseOccasion) = coroutineScope.launch { exerciseOccasionDao.delete(exerciseOccasion) }

    //endregion

    //region ExerciseProgram

    fun insertExerciseProgram(exerciseProgram : ExerciseProgram) = coroutineScope.launch {
        exerciseProgramDao.insertExerciseProgram(exerciseProgram)
    }

    fun updateExerciseProgram(exerciseProgram : ExerciseProgram) = coroutineScope.launch {
        exerciseProgramDao.update(exerciseProgram)
    }

    fun getAllExerciseProgramsWithExercises() : Flow<List<ExerciseProgramWithExercises>> = exerciseProgramWithExercisesDao.getAll()

    //endregion

    // region SimpleTask

    suspend fun insertSimpleTask(simpleTask : SimpleTask) = coroutineScope.launch { simpleTaskDao.insertSimpleTask(simpleTask) }

    fun updateSimpleTask(simpleTask : SimpleTask) = coroutineScope.launch { simpleTaskDao.update(simpleTask) }

    fun getAllSimpleTask() : Flow<List<SimpleTask>> = simpleTaskDao.getAll()

    //endregion

    //region Task

    suspend fun insertTask() : Long = withContext(Dispatchers.IO) {
        taskDao.insert(Task())
    }

    fun deleteTask(task : Task) = coroutineScope.launch { taskDao.delete(task) }

    fun getAllTasks() : Flow<List<Task>> = taskDao.getAll()

    //endregion

    //region TaskOccasion

    fun getAllTaskOccasions() : Flow<List<TaskOccasion>> = taskOccasionDao.getAll()

    fun insertTaskOccasion(taskOccasion : TaskOccasion) = coroutineScope.launch {
        taskOccasionDao.insert(taskOccasion)
    }

    fun updateTaskOccasion(taskOccasion : TaskOccasion) = coroutineScope.launch {
        taskOccasionDao.update(taskOccasion)
    }

    fun deleteTaskOccasion(taskOccasion : TaskOccasion) = coroutineScope.launch {
        taskOccasionDao.delete(taskOccasion)
    }

    fun deleteTaskOccasionWithTaskIdAndDateFrom(taskOccasion : TaskOccasion) = coroutineScope.launch {
        taskOccasion.dateFrom?.let {
            taskOccasionDao.deleteWithTaskIdAndDateTimeFrom(
                taskOccasion.taskId,
                it
            )
        }
    }

    //endregion

    //region TaskWithOccasions

    fun getAllTasksWithOccasions() : Flow<List<TaskWithOccasions>> = taskWithOccasionsDao.getAll()

    //endregion

    //region WeekdaySchedule

    fun insertWeekdayScheduleEntry(weekdayScheduleEntry : WeekdaySchedule) = coroutineScope.launch { weekdayScheduleDao.insert(weekdayScheduleEntry) }

    fun deleteWeekdayScheduleEntry(weekdayScheduleEntry : WeekdaySchedule) = coroutineScope.launch { weekdayScheduleDao.delete(weekdayScheduleEntry) }

    //endregion
}