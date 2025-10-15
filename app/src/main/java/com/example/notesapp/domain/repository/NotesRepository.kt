package com.example.notesapp.domain.repository

import com.example.notesapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: String): Note?
    suspend fun addNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun syncNotes()
    suspend fun getUnsyncedNotes(): List<Note>
}