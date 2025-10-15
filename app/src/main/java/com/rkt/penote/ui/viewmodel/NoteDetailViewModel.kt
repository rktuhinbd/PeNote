package com.rkt.penote.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rkt.penote.domain.model.Note
import com.rkt.penote.domain.usecase.GetNotesUseCase
import com.rkt.penote.domain.usecase.UpdateNoteUseCase
import com.rkt.penote.ui.state.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<Note?>>(UIState.Loading)
    val uiState: StateFlow<UIState<Note?>> = _uiState.asStateFlow()

    fun loadNote(noteId: String) {
        viewModelScope.launch {
            getNotesUseCase()
                .catch { e ->
                    _uiState.value = UIState.Error(e.message ?: "Unknown error")
                }
                .collect { notes ->
                    val note = notes.find { it.id == noteId }
                    _uiState.value = UIState.Success(note)
                }
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            try {
                val updatedNote = note.copy(updatedAt = Date(), isSynced = false)
                updateNoteUseCase(updatedNote)
                _uiState.value = UIState.Success(updatedNote)
            } catch (e: Exception) {
                _uiState.value = UIState.Error(e.message ?: "Failed to update note")
            }
        }
    }
}