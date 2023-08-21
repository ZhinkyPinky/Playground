package com.je.playground.ui

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.je.playground.R
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

class Notifications(private val application : Application) {
    companion object {
        const val CHANNEL_ID = "task-reminders"
    }

    private val notificationManager : NotificationManager by lazy {
        application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun scheduleNotification(
        taskId : Long,
        taskName : String,
        notificationDateTime : LocalDateTime
    ) {
        // Create the notification channel, if necessary
        createNotificationChannel()

        // Convert the notification date and time to a timestamp
        val notificationTimestamp = notificationDateTime
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        // Create the notification data
        val data = Data
            .Builder()
            .putInt(
                NotificationWorker.NOTIFICATION_ID,
                1
            )
            .putString(
                NotificationWorker.TASK_NAME,
                taskName
            )
            .build()

        // Create the work request
        val workRequest = OneTimeWorkRequest
            .Builder(NotificationWorker::class.java)
            .setInitialDelay(
                notificationTimestamp - System.currentTimeMillis(),
                TimeUnit.MILLISECONDS
            )
            .setInputData(data)
            .addTag(taskId.toString())
            .build()

        // Schedule the work
        WorkManager
            .getInstance(application)
            .enqueue(workRequest)
    }

    fun cancelNotification(taskId : Long) {
        WorkManager
            .getInstance(application)
            .cancelAllWorkByTag(taskId.toString())
    }

    private fun createNotificationChannel() {
        // Check if the notification channel exists
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            // Create the notification channel
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Task Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for tasks"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}

class NotificationWorker(
    context : Context,
    workerParams : WorkerParameters
) : Worker(
    context,
    workerParams
) {

    companion object {
        const val NOTIFICATION_ID = "notification-id"
        const val NOTIFICATION = "notification"
        const val TASK_NAME = "task-name"
    }

    override fun doWork() : Result {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = inputData.getInt(
            NOTIFICATION_ID,
            0
        )
        val taskName = inputData.getString(TASK_NAME)

        if (taskName != null && notificationId != 0) {
            val resultIntent = Intent(
                applicationContext,
                MainActivity::class.java
            )
            val resultPendingIntent : PendingIntent? = TaskStackBuilder
                .create(applicationContext)
                .run {
                    addNextIntentWithParentStack(resultIntent)
                    getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                }

            // Create the notification builder
            val builder = NotificationCompat
                .Builder(
                    applicationContext,
                    Notifications.CHANNEL_ID
                )
                .setSmallIcon(R.mipmap.ic_notification_foreground)
                .setContentTitle("Task Reminder")
                .setContentText("Don't forget to complete task: $taskName")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)

            notificationManager.notify(
                notificationId,
                builder.build()
            )
        }

        return Result.success()
    }
}