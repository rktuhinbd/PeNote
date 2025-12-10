package com.rkt.penote.data.database

import androidx.room.TypeConverter
import com.rkt.penote.domain.entity.CheckListItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromCheckList(list: List<CheckListItem>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toCheckList(value: String): List<CheckListItem> {
        return Json.decodeFromString(value)
    }
}
