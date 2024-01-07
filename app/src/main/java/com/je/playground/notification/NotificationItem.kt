package com.je.playground.notification

import java.time.LocalDateTime

data class NotificationItem(
    val id : Int,
    val title : String,
    val message : String,
    val dateTime : LocalDateTime,
)
