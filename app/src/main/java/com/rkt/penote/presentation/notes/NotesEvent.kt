package com.rkt.penote.presentation.notes

import com.rkt.penote.domain.entity.Note
import com.rkt.penote.domain.entity.NoteType

sealed class NotesEvent {
    data class DeleteNote(val note: Note) : NotesEvent()
    object RestoreNote : NotesEvent()
    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    data class ToggleOrderSection(val isExpanded: Boolean) : NotesEvent()
}

sealed class NoteOrder(val orderType: OrderType) {
    class Title(orderType: OrderType): NoteOrder(orderType)
    class Date(orderType: OrderType): NoteOrder(orderType)
    class Color(orderType: OrderType): NoteOrder(orderType)

    fun copy(orderType: OrderType): NoteOrder {
        return when(this) {
            is Title -> Title(orderType)
            is Date -> Date(orderType)
            is Color -> Color(orderType)
        }
    }
}

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
