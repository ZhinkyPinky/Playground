package com.je.playground

import android.content.Context
import com.je.playground.databaseV2.AppDatabaseV2
import com.je.playground.databaseV2.repository.TasksRepositoryV2

object GraphV2 {
    lateinit var database : AppDatabaseV2

    val tasksRepositoryV2 by lazy {
        TasksRepositoryV2(
            exerciseDao = database.getExerciseDao(),
            exerciseOccasionDao = database.getExerciseOccasionDao(),
            exerciseProgramDao = database.getExerciseProgramDao(),
            exerciseProgramWithExercisesDao = database.getExerciseProgramWithExercisesDao(),
            simpleTaskDao = database.getSimpleTaskDao(),
            taskDao = database.getTaskDao(),
            taskOccasionDao = database.getTaskOccasionDao(),
            taskWithOccasionsDao = database.getTaskWithOccasionDao(),
            weekdayScheduleDao = database.getWeekdayScheduleDao()
        )
    }

    fun provide(context : Context) {
        database = AppDatabaseV2.getDatabase(context)
    }
}