package com.je.playground.notification

interface INotificationScheduler {
    fun scheduleNotification(
        notificationItem : NotificationItem
    )

    fun cancelNotification(id : Int)
}