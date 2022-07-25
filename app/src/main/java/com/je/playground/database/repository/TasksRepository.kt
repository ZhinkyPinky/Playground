package com.je.playground.database.repository

import com.je.playground.database.tasks.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TasksRepository(
    private val simpleTaskDao : SimpleTaskDao,
    private val exerciseDao : ExerciseDao,
    private val exerciseProgramWithExercisesDao : ExerciseProgramWithExercisesDao,
    private val exerciseProgramExerciseConnectionDao : ExerciseProgramExerciseConnectionDao,
    private val exerciseProgramWithExercisesAndConnectionDao : ExerciseProgramWithExercisesAndConnectionDao
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun getSimpleTasks() : Flow<List<SimpleTask>> = simpleTaskDao.getAllSimpleTasks()

    fun insertExercise(exercise : Exercise) {
        coroutineScope.launch(Dispatchers.IO) {
            exerciseDao.insertExercise(exercise)
        }
    }

    fun getExercises() : Flow<List<Exercise>> = exerciseDao.getAllExercises()

    fun getExerciseProgramExerciseConnections() : Flow<List<ExerciseProgramExerciseConnection>> = exerciseProgramExerciseConnectionDao.getAllConnections()

    fun getExerciseProgramsWithExercises() : Flow<List<ExerciseProgramWithExercises>> = exerciseProgramWithExercisesDao.getAllExerciseProgramsWithExercises()

    fun getExerciseProgramsWithExercisesAndConnection() : Flow<List<ExerciseProgramWithExercisesAndConnections>> = exerciseProgramWithExercisesAndConnectionDao.getAll()

    suspend fun updateExerciseProgramExerciseConnection(exerciseProgramExerciseConnection : ExerciseProgramExerciseConnection) {
        exerciseProgramExerciseConnectionDao.update(exerciseProgramExerciseConnection)
    }


}