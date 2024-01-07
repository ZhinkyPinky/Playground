package com.je.playground.notification

import java.time.LocalDateTime

interface INotificationScheduler {
    fun scheduleNotification(
        id : Long,
        title : String,
        message : String,
        dateTime : LocalDateTime
    )

    fun cancel(id : Long)
}