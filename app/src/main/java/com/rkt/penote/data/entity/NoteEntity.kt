package com.rkt.penote.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rkt.penote.domain.entity.CheckListItem
import com.rkt.penote.domain.entity.Note
import com.rkt.penote.domain.entity.NoteType

/**
 * Room Entity representing a Note in the database.
 */
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
    /**
     * Mapper function to convert [NoteEntity] to domain [Note].
     */
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

/**
 * Mapper extension function to convert domain [Note] to [NoteEntity].
 */
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
