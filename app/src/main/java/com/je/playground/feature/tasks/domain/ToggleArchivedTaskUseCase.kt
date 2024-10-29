package com.je.playground.feature.tasks.domain

import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.repository.TaskRepository
import com.je.playground.notification.NotificationScheduler
import java.time.LocalDateTime
import javax.inject.Inject

class ToggleTaskIsArchivedUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val notificationScheduler: NotificationScheduler
) {
    operator fun invoke(task: Task) = taskRepository.updateTask(task = task.apply {
        isArchived = !isArchived

        if (isArchived) archivedDateTime = LocalDateTime.now()
        else notificationScheduler.cancelNotification(task.taskId.toInt())
    })
}