package com.je.playground.hilt

import com.je.playground.database.exerciseprogram.dao.ExerciseDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramWithExercisesDao
import com.je.playground.database.exerciseprogram.repository.ExerciseProgramRepository
import com.je.playground.database.tasks.dao.*
import com.je.playground.database.tasks.repository.TasksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @Provides
    @ViewModelScoped
    fun providesTasksRepository(
        taskDao : TaskDao,
        taskGroupDao : TaskGroupDao,
    ) : TasksRepository {
        return TasksRepository(
            taskGroupDao = taskGroupDao,
            taskDao = taskDao
        )
    }

    @Provides
    @ViewModelScoped
    fun providesExerciseProgramRepository(
        exerciseDao : ExerciseDao,
        exerciseProgramDao : ExerciseProgramDao,
        exerciseProgramWithExercisesDao : ExerciseProgramWithExercisesDao,
    ) : ExerciseProgramRepository {
        return ExerciseProgramRepository(
            exerciseDao = exerciseDao,
            exerciseProgramDao = exerciseProgramDao,
            exerciseProgramWithExercisesDao = exerciseProgramWithExercisesDao
        )
    }
}
