package com.rkt.penote.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState
import com.rkt.penote.domain.entity.CheckListItem
import com.rkt.penote.domain.entity.NoteType

sealed class AddEditNoteEvent {
    data class EnteredTitle(val value: String) : AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteEvent()
    data class EnteredContent(val value: String) : AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState) : AddEditNoteEvent()
    data class ChangeColor(val color: Int) : AddEditNoteEvent()
    object SaveNote : AddEditNoteEvent()
    data class ChangeNoteType(val type: NoteType) : AddEditNoteEvent()
    data class AddCheckListItem(val text: String) : AddEditNoteEvent()
    data class ToggleCheckListItem(val index: Int) : AddEditNoteEvent()
    data class RemoveCheckListItem(val index: Int) : AddEditNoteEvent()
    data class EnteredCheckListItem(val index: Int, val text: String) : AddEditNoteEvent()
}
