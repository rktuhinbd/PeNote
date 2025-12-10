package com.rkt.penote.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
data class Note(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    val type: NoteType,
    val checkList: List<CheckListItem> = emptyList()
)

@Serializable
enum class NoteType {
    NORMAL, CHECKBOX
}

@Serializable
data class CheckListItem(
    val text: String,
    val isChecked: Boolean
)
