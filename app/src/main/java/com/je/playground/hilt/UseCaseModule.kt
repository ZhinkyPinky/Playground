package com.je.playground.hilt

import com.je.playground.database.tasks.repository.SubTaskRepository
import com.je.playground.database.tasks.repository.TaskRepository
import com.je.playground.database.tasks.repository.TaskWithSubTasksRepository
import com.je.playground.feature.tasks.domain.GetActiveTasksWithSubTasksUseCase
import com.je.playground.feature.tasks.domain.GetTaskUseCase
import com.je.playground.feature.tasks.domain.GetTaskWithSubTasksByTaskIdUseCase
import com.je.playground.feature.tasks.domain.SaveTaskUseCase
import com.je.playground.feature.tasks.domain.ToggleSubTaskIsCompletedUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun providesAddTaskUseCase(taskRepository : TaskRepository) : SaveTaskUseCase {
        return SaveTaskUseCase(taskRepository)
    }

    @Provides
    @Singleton
    fun providesGetActiveTasksWithSubTasksUseCase(taskWithSubTasksRepository : TaskWithSubTasksRepository) : GetActiveTasksWithSubTasksUseCase {
        return GetActiveTasksWithSubTasksUseCase(taskWithSubTasksRepository)
    }

    @Provides
    @Singleton
    fun providesGetTaskUseCase(taskRepository : TaskRepository) : GetTaskUseCase {
        return GetTaskUseCase(taskRepository)
    }


    @Provides
    @Singleton
    fun providesGetTaskWithSubTasksByTaskIdUseCase(taskWithSubTasksRepository : TaskWithSubTasksRepository) : GetTaskWithSubTasksByTaskIdUseCase {
        return GetTaskWithSubTasksByTaskIdUseCase(taskWithSubTasksRepository)
    }

    @Provides
    @Singleton
    fun providesToggleSubTaskCompletionUseCase(
        subTaskRepository : SubTaskRepository,
        taskRepository : TaskRepository
    ) : ToggleSubTaskIsCompletedUseCase {
        return ToggleSubTaskIsCompletedUseCase(
            subTaskRepository = subTaskRepository,
            taskRepository = taskRepository
        )
    }
}