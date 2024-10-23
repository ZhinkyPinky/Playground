package com.je.playground.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.ZoneId

class NotificationScheduler(
    private val context : Context
) : INotificationScheduler {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun scheduleNotification(
        notificationItem : NotificationItem
    ) {
        val intent = Intent(
            context,
            NotificationReceiver::class.java
        ).apply {
            putExtra(
                "ID",
                notificationItem.id
            )

            putExtra(
                "TITLE",
                notificationItem.title
            )

            putExtra(
                "MESSAGE",
                notificationItem.message
            )
        }

        Any()

        notificationItem.hashCode()

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            notificationItem.dateTime
                .atZone(ZoneId.systemDefault())
                .toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                notificationItem.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    @Override
    override fun cancelNotification(id : Int) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                id,
                Intent(
                    context,
                    NotificationReceiver::class.java
                ),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}