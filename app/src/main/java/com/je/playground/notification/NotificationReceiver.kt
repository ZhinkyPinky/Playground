package com.je.playground.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.je.playground.MainActivity
import com.je.playground.R

class NotificationReceiver : BroadcastReceiver() {
    companion object {
        const val CHANNEL_ID = "task-reminders"
        const val TASK_REMINDER = "task_reminder"
        const val NOTIFICATION_ID = "notification_id"
        const val TITLE = "title"
    }

    override fun onReceive(
        context : Context?,
        intent : Intent?
    ) {
        if (context == null || intent == null) return

        val id = intent.getIntExtra(
            "ID",
            0
        )

        val title = intent.getStringExtra("TITLE") ?: return
        val message = intent.getStringExtra("MESSAGE")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(notificationManager)

        Log.d(
            "notification",
            "2 -  $message"
        )
        if (id != 0) {
            val resultIntent = Intent(
                context,
                MainActivity::class.java
            )

            val resultPendingIntent : PendingIntent? = TaskStackBuilder
                .create(context)
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
                    context,
                    CHANNEL_ID
                )
                .setSmallIcon(R.mipmap.ic_notification_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(
                    NotificationCompat
                        .BigTextStyle()
                        .bigText(message)
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)

            notificationManager.notify(
                id,
                builder.build()
            )
        }
    }

    private fun createNotificationChannel(notificationManager : NotificationManager) {
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