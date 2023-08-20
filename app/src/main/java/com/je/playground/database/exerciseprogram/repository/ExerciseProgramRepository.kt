package com.je.playground.database.exerciseprogram.repository

import com.je.playground.database.exerciseprogram.dao.ExerciseDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramWithExercisesDao
import com.je.playground.database.exerciseprogram.entity.Exercise
import com.je.playground.database.exerciseprogram.entity.ExerciseProgram
import com.je.playground.database.exerciseprogram.entity.ExerciseProgramWithExercises
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


class ExerciseProgramRepository @Inject constructor(
    private val exerciseProgramWithExercisesDao : ExerciseProgramWithExercisesDao,
    private val exerciseProgramDao : ExerciseProgramDao,
    private val exerciseDao : ExerciseDao
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun getExerciseWithExercises() : Flow<List<ExerciseProgramWithExercises>> = exerciseProgramWithExercisesDao.getAll()

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

    //region ExerciseProgram

    fun insertExerciseProgram(exerciseProgram : ExerciseProgram) = coroutineScope.launch {
        exerciseProgramDao.insertExerciseProgram(exerciseProgram)
    }

    fun updateExerciseProgram(exerciseProgram : ExerciseProgram) = coroutineScope.launch {
        exerciseProgramDao.update(exerciseProgram)
    }

    fun getAllExerciseProgramsWithExercises() : Flow<List<ExerciseProgramWithExercises>> = exerciseProgramWithExercisesDao.getAll()

    //endregion
}