package com.rkt.penote.domain.entity

import kotlinx.serialization.Serializable

/**
 * Domain entity representing a Note in the application.
 *
 * @param id The unique identifier of the note.
 * @param title The title of the note.
 * @param content The content of the note (for normal notes).
 * @param timestamp The last modification timestamp.
 * @param color The color of the note.
 * @param type The type of the note (Normal or Checkbox).
 * @param checkList The list of items (for checkbox notes).
 */
data class Note(
    val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    val type: NoteType,
    val checkList: List<CheckListItem> = emptyList()
)

/**
 * Enum defining the type of a note.
 */
@Serializable
enum class NoteType {
    NORMAL, CHECKBOX
}

/**
 * Data class representing an item in a checkbox note.
 *
 * @param text The text content of the item.
 * @param isChecked Whether the item is checked/completed.
 */
@Serializable
data class CheckListItem(
    val text: String,
    val isChecked: Boolean
)
