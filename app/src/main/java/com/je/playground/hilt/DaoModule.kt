package com.je.playground.hilt

import com.je.playground.databaseV2.AppDatabaseV2
import com.je.playground.databaseV2.tasks.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {
    @Provides
    @Singleton
    fun provideExerciseDao(appDatabaseV2 : AppDatabaseV2) : ExerciseDao {
        return appDatabaseV2.getExerciseDao()
    }

    @Provides
    @Singleton
    fun bindExerciseOccasionDao(appDatabaseV2 : AppDatabaseV2) : ExerciseOccasionDao {
        return appDatabaseV2.getExerciseOccasionDao()
    }

    @Provides
    @Singleton
    fun bindExerciseProgramDao(appDatabaseV2 : AppDatabaseV2) : ExerciseProgramDao {
        return appDatabaseV2.getExerciseProgramDao()
    }

    @Provides
    @Singleton
    fun bindExerciseProgramWithExercisesDao(appDatabaseV2 : AppDatabaseV2) : ExerciseProgramWithExercisesDao {
        return appDatabaseV2.getExerciseProgramWithExercisesDao()
    }

    @Provides
    @Singleton
    fun bindSimpleTaskDao(appDatabaseV2 : AppDatabaseV2) : SimpleTaskDao {
        return appDatabaseV2.getSimpleTaskDao()
    }

    @Provides
    @Singleton
    fun bindTaskDao(appDatabaseV2 : AppDatabaseV2) : TaskDao {
        return appDatabaseV2.getTaskDao()
    }

    @Provides
    @Singleton
    fun bindTaskOccasionDao(appDatabaseV2 : AppDatabaseV2) : TaskOccasionDao {
        return appDatabaseV2.getTaskOccasionDao()
    }

    @Provides
    @Singleton
    fun bindTaskWithOccasionsDao(appDatabaseV2 : AppDatabaseV2) : TaskWithOccasionsDao {
        return appDatabaseV2.getTaskWithOccasionDao()
    }

    @Provides
    @Singleton
    fun bindWeekdayScheduleDao(appDatabaseV2 : AppDatabaseV2) : WeekdayScheduleDao {
        return appDatabaseV2.getWeekdayScheduleDao()
    }
}