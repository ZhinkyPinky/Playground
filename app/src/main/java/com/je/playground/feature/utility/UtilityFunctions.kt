package com.je.playground.feature.utility

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun LocalDate.toEpochMilliseconds(): Long? =
    this.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()

fun Long.toLocalDate(): LocalDate? =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
