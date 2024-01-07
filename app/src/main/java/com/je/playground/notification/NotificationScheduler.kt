package com.je.playground.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.LocalDateTime
import java.time.ZoneId

class NotificationScheduler(
    private val context : Context
) : INotificationScheduler {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun scheduleNotification(
        id : Long,
        title : String,
        message : String,
        dateTime : LocalDateTime
    ) {
        val intent = Intent(
            context,
            NotificationReceiver::class.java
        ).apply {
            putExtra(
                "ID",
                id.toInt()
            )

            putExtra(
                "TITLE",
                title
            )

            putExtra(
                "MESSAGE",
                message
            )
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            dateTime
                .atZone(ZoneId.systemDefault())
                .toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    @Override
    override fun cancel(id : Long) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                id.toInt(),
                Intent(
                    context,
                    NotificationReceiver::class.java
                ),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}