package com.je.playground.databaseV2.exerciseprogram.repository

import com.je.playground.databaseV2.exerciseprogram.dao.ExerciseProgramV2Dao
import com.je.playground.databaseV2.exerciseprogram.dao.ExerciseProgramWithExercisesV2Dao
import com.je.playground.databaseV2.exerciseprogram.dao.ExerciseV2Dao
import com.je.playground.databaseV2.exerciseprogram.entity.ExerciseProgramWithExercisesV2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ExerciseProgramRepository @Inject constructor(
    private val exerciseProgramWithExercisesV2Dao : ExerciseProgramWithExercisesV2Dao,
    private val exerciseProgramV2Dao : ExerciseProgramV2Dao,
    private val exerciseV2Dao : ExerciseV2Dao
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun getExerciseWithExercises() : Flow<List<ExerciseProgramWithExercisesV2>> = exerciseProgramWithExercisesV2Dao.getAll()
}