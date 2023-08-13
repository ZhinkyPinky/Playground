package com.je.playground.hilt

import com.je.playground.databaseV2.exerciseprogram.dao.ExerciseProgramV2Dao
import com.je.playground.databaseV2.exerciseprogram.dao.ExerciseProgramWithExercisesV2Dao
import com.je.playground.databaseV2.exerciseprogram.dao.ExerciseV2Dao
import com.je.playground.databaseV2.exerciseprogram.repository.ExerciseProgramRepository
import com.je.playground.databaseV2.tasks.dao.*
import com.je.playground.databaseV2.tasks.repository.TasksRepositoryV2
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
        exerciseDao : ExerciseDao,
        exerciseOccasionDao : ExerciseOccasionDao,
        exerciseProgramDao : ExerciseProgramDao,
        exerciseProgramWithExercisesDao : ExerciseProgramWithExercisesDao,
        simpleTaskDao : SimpleTaskDao,
        taskDao : TaskDao,
        taskOccasionDao : TaskOccasionDao,
        taskWithOccasionsDao : TaskWithOccasionsDao,
        weekdayScheduleDao : WeekdayScheduleDao
    ) : TasksRepositoryV2 {
        return TasksRepositoryV2(
            exerciseDao,
            exerciseOccasionDao,
            exerciseProgramDao,
            exerciseProgramWithExercisesDao,
            simpleTaskDao,
            taskDao,
            taskOccasionDao,
            taskWithOccasionsDao,
            weekdayScheduleDao
        )
    }

    @Provides
    @ViewModelScoped
    fun providesExerciseProgramRepository(
        exerciseProgramWithExercisesV2Dao : ExerciseProgramWithExercisesV2Dao,
        exerciseProgramV2Dao : ExerciseProgramV2Dao,
        exerciseV2Dao : ExerciseV2Dao,
    ) : ExerciseProgramRepository {
        return ExerciseProgramRepository(
            exerciseProgramWithExercisesV2Dao,
            exerciseProgramV2Dao,
            exerciseV2Dao
        )
    }
}
