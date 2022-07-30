package com.je.playground.databaseV2

import androidx.room.TypeConverter
import java.time.*
import java.time.format.DateTimeFormatter

object DateTimeTypeConverters {
    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value : String?) : OffsetDateTime? {
        return value?.let { OffsetDateTime.parse(it) }
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date : OffsetDateTime?) : String? {
        return date?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    @TypeConverter
    @JvmStatic
    fun toLocalDateTime(value : String?) : LocalDateTime? {
        return value?.let { LocalDateTime.parse(value) }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalDateTime(value : LocalDateTime?) : String? {
        return value?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @TypeConverter
    @JvmStatic
    fun toLocalDate(value : String?) : LocalDate? {
        return value?.let { LocalDate.parse(value) }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalDate(value : LocalDate?) : String? {
        return value?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @TypeConverter
    @JvmStatic
    fun toLocalTime(value : String?) : LocalTime? {
        return value?.let { LocalTime.parse(value) }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalTime(value : LocalTime?) : String? {
        return value?.format(DateTimeFormatter.ISO_LOCAL_TIME)
    }

    @TypeConverter
    @JvmStatic
    fun toDuration(value : Long?) : Duration? {
        return value?.let { Duration.ofMillis(it) }
    }

    @TypeConverter
    @JvmStatic
    fun fromDuration(value : Duration?) : Long? {
        return value?.toMillis()
    }
}