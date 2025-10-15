package com.rkt.penote.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rkt.penote.data.connectivity.ConnectivityManager
import com.rkt.penote.data.sync.SyncManager
import com.rkt.penote.domain.model.Note
import com.rkt.penote.domain.usecase.*
import com.rkt.penote.ui.state.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val syncNotesUseCase: SyncNotesUseCase,
    private val connectivityManager: ConnectivityManager,
    private val syncManager: SyncManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<List<Note>>>(UIState.Loading)
    val uiState: StateFlow<UIState<List<Note>>> = _uiState.asStateFlow()

    private val _isOffline = MutableStateFlow(false)
    val isOffline: StateFlow<Boolean> = _isOffline.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    init {
        observeNotes()
        observeConnectivity()
        startPeriodicSync()
    }

    private fun observeNotes() {
        viewModelScope.launch {
            getNotesUseCase()
                .catch { e ->
                    _uiState.value = UIState.Error(e.message ?: "Unknown error")
                }
                .collect { notes ->
                    _uiState.value = UIState.Success(notes)
                }
        }
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityManager.networkStateFlow()
                .collect { isConnected ->
                    _isOffline.value = !isConnected
                    if (isConnected) {
                        syncNotes()
                    }
                }
        }
    }

    private fun startPeriodicSync() {
        syncManager.enqueuePeriodicSync()
    }

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            try {
                val note = Note(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    content = content,
                    createdAt = Date(),
                    updatedAt = Date(),
                    isSynced = false
                )
                addNoteUseCase(note)
            } catch (e: Exception) {
                _uiState.value = UIState.Error(e.message ?: "Failed to add note")
            }
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            try {
                val updatedNote = note.copy(updatedAt = Date(), isSynced = false)
                updateNoteUseCase(updatedNote)
            } catch (e: Exception) {
                _uiState.value = UIState.Error(e.message ?: "Failed to update note")
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                deleteNoteUseCase(note)
            } catch (e: Exception) {
                _uiState.value = UIState.Error(e.message ?: "Failed to delete note")
            }
        }
    }

    private fun syncNotes() {
        viewModelScope.launch {
            try {
                _isSyncing.value = true
                syncNotesUseCase()
            } catch (e: Exception) {
                // Sync failed, but don't show error to user
                // Notes will be synced when network is available
            } finally {
                _isSyncing.value = false
            }
        }
    }

    fun retrySync() {
        syncManager.enqueueSync()
    }
}