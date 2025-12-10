package com.rkt.penote.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rkt.penote.domain.entity.CheckListItem
import com.rkt.penote.domain.entity.Note
import com.rkt.penote.domain.entity.NoteType
import com.rkt.penote.domain.usecase.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(AddEditNoteState())
    val state: State<AddEditNoteState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _state.value = state.value.copy(
                            title = note.title,
                            isTitleHintVisible = false,
                            content = note.content,
                            isContentHintVisible = false,
                            noteColor = note.color,
                            noteType = note.type,
                            checkList = note.checkList
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _state.value = state.value.copy(
                    title = event.value
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _state.value = state.value.copy(
                    isTitleHintVisible = !event.focusState.isFocused &&
                            state.value.title.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredContent -> {
                _state.value = state.value.copy(
                    content = event.value
                )
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                _state.value = state.value.copy(
                    isContentHintVisible = !event.focusState.isFocused &&
                            state.value.content.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeColor -> {
                _state.value = state.value.copy(
                    noteColor = event.color
                )
            }
            is AddEditNoteEvent.ChangeNoteType -> {
                 _state.value = state.value.copy(
                    noteType = event.type
                )
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = state.value.title,
                                content = state.value.content,
                                timestamp = System.currentTimeMillis(),
                                color = state.value.noteColor,
                                type = state.value.noteType,
                                checkList = state.value.checkList,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: Exception) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
            is AddEditNoteEvent.AddCheckListItem -> {
                 val newList = state.value.checkList.toMutableList()
                 newList.add(CheckListItem(event.text, false))
                 _state.value = state.value.copy(checkList = newList)
            }
            is AddEditNoteEvent.RemoveCheckListItem -> {
                val newList = state.value.checkList.toMutableList()
                if (event.index in newList.indices) {
                    newList.removeAt(event.index)
                    _state.value = state.value.copy(checkList = newList)
                }
            }
             is AddEditNoteEvent.ToggleCheckListItem -> {
                 val newList = state.value.checkList.toMutableList()
                 if (event.index in newList.indices) {
                     val item = newList[event.index]
                     newList[event.index] = item.copy(isChecked = !item.isChecked)
                     _state.value = state.value.copy(checkList = newList)
                 }
             }
             is AddEditNoteEvent.EnteredCheckListItem -> {
                 val newList = state.value.checkList.toMutableList()
                 if(event.index in newList.indices) {
                     newList[event.index] = newList[event.index].copy(text = event.text)
                     _state.value = state.value.copy(checkList = newList)
                 }
             }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}
