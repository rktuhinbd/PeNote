package com.rkt.penote.presentation.add_edit_note

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.rkt.penote.domain.entity.CheckListItem
import com.rkt.penote.domain.entity.NoteType
import com.rkt.penote.presentation.ui.theme.NoteColors

data class AddEditNoteState(
    val noteType: NoteType = NoteType.NORMAL,
    val title: String = "",
    val titleHint: String = "Enter title...",
    val isTitleHintVisible: Boolean = true,
    val content: String = "",
    val contentHint: String = "Enter some content",
    val isContentHintVisible: Boolean = true,
    val noteColor: Int = NoteColors.random().toArgb(),
    val checkList: List<CheckListItem> = emptyList()
)
