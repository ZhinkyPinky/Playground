package com.je.playground.database.exerciseprogram.repository

import com.je.playground.database.exerciseprogram.dao.ExerciseDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramWeekdayScheduleDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramWithAllTheThingsDao
import com.je.playground.database.exerciseprogram.entity.Exercise
import com.je.playground.database.exerciseprogram.entity.ExerciseProgram
import com.je.playground.database.exerciseprogram.entity.ExerciseProgramWeekdaySchedule
import com.je.playground.database.exerciseprogram.entity.ExerciseProgramWithAllTheThings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ExerciseProgramRepository @Inject constructor(
    private val exerciseProgramWithAllTheThingsDao : ExerciseProgramWithAllTheThingsDao,
    private val exerciseProgramDao : ExerciseProgramDao,
    private val exerciseDao : ExerciseDao,
    private val exerciseProgramWeekdayScheduleDao : ExerciseProgramWeekdayScheduleDao
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun getExerciseWithExercises() : Flow<List<ExerciseProgramWithAllTheThings>> = exerciseProgramWithAllTheThingsDao.getAll()

    suspend fun insertExercise(exercise : Exercise) : Long = withContext(Dispatchers.IO) {
        exerciseDao.insertExercise(exercise)
    }

    suspend fun updateExercise(exercise : Exercise) = withContext(Dispatchers.IO) {
        exerciseDao.updateExercise(exercise)
    }

    fun deleteExercise(exercise : Exercise) = coroutineScope.launch {
        exerciseDao.delete(exercise)
    }

    suspend fun insertExerciseProgram(exerciseProgram : ExerciseProgram) = withContext(Dispatchers.IO) {
        exerciseProgramDao.insertExerciseProgram(exerciseProgram)
    }

    suspend fun updateExerciseProgram(exerciseProgram : ExerciseProgram) = withContext(Dispatchers.IO) {
        exerciseProgramDao.update(exerciseProgram)
    }

    fun getAllExerciseProgramsWithAllTheThings() : Flow<List<ExerciseProgramWithAllTheThings>> = exerciseProgramWithAllTheThingsDao.getAll()

    suspend fun insertExerciseProgramWeekdaySchedule(exerciseProgramWeekdayScheduleEntry : ExerciseProgramWeekdaySchedule) = withContext(Dispatchers.IO) { exerciseProgramWeekdayScheduleDao.insert(exerciseProgramWeekdayScheduleEntry) }

    suspend fun selectExerciseProgramWithAllTheThingsById(exerciseProgramId: Long) = exerciseProgramWithAllTheThingsDao.selectById(exerciseProgramId)
}