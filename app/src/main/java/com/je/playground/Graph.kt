package com.je.playground

import android.content.Context
import com.je.playground.database.AppDatabase
import com.je.playground.database.repository.TasksRepository

object Graph {
    lateinit var database : AppDatabase

    val tasksRepository by lazy {
        TasksRepository(
            simpleTaskDao = database.getSimpleTaskDao(),
            exerciseDao = database.getExerciseDao(),
            exerciseProgramExerciseConnectionDao = database.getExerciseProgramExerciseConnectionDao(),
            exerciseProgramWithExercisesAndConnectionsDao = database.getExerciseProgramWithExercisesAndConnectionsDao(),
        )
    }

    fun provide(context : Context) {
        database = AppDatabase.getDatabase(context)
    }
}