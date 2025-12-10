package com.rkt.penote.domain.repository

import com.rkt.penote.domain.entity.Note
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Note operations.
 */
interface NoteRepository {

    /**
     * Retrieves all notes as a Flow.
     */
    fun getNotes(): Flow<List<Note>>

    /**
     * Retrieves a note by its ID.
     */
    suspend fun getNoteById(id: Int): Note?

    /**
     * Inserts or updates a note.
     */
    suspend fun insertNote(note: Note)

    /**
     * Deletes a note.
     */
    suspend fun deleteNote(note: Note)
}
