package com.je.playground.hilt

import com.je.playground.databaseV2.repository.TasksRepositoryV2
import com.je.playground.databaseV2.tasks.dao.*
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
}