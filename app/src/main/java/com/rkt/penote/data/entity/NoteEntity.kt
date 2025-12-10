package com.rkt.penote.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rkt.penote.domain.entity.CheckListItem
import com.rkt.penote.domain.entity.Note
import com.rkt.penote.domain.entity.NoteType

@Entity(tableName = "note")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    val type: NoteType,
    val checkList: List<CheckListItem> = emptyList()
) {
    fun toNote(): Note {
        return Note(
            id = id,
            title = title,
            content = content,
            timestamp = timestamp,
            color = color,
            type = type,
            checkList = checkList
        )
    }
}

fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        timestamp = timestamp,
        color = color,
        type = type,
        checkList = checkList
    )
}
