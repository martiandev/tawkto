package com.tawk.to.mars.git.model.entity.converter

import androidx.room.TypeConverter
import java.util.*

class DateConverter
{

    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return if (date == null) null else date.getTime()
    }
}