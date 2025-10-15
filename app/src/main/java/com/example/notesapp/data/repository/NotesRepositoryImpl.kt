package com.example.notesapp.data.repository

import com.example.notesapp.data.local.dao.NoteDao
import com.example.notesapp.data.mapper.NoteMapper
import com.example.notesapp.data.remote.api.NotesApiService
import com.example.notesapp.data.remote.dto.SyncRequestDto
import com.example.notesapp.domain.model.Note
import com.example.notesapp.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val apiService: NotesApiService
) : NotesRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.map { NoteMapper.entityToDomain(it) }
        }
    }

    override suspend fun getNoteById(id: String): Note? {
        return noteDao.getNoteById(id)?.let { NoteMapper.entityToDomain(it) }
    }

    override suspend fun addNote(note: Note) {
        noteDao.insertNote(NoteMapper.domainToEntity(note))
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(NoteMapper.domainToEntity(note))
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(NoteMapper.domainToEntity(note))
    }

    override suspend fun syncNotes() {
        try {
            // Get unsynced notes
            val unsyncedNotes = noteDao.getUnsyncedNotes()
            if (unsyncedNotes.isNotEmpty()) {
                val syncRequest = SyncRequestDto(
                    notes = unsyncedNotes.map { NoteMapper.domainToDto(NoteMapper.entityToDomain(it)) }
                )
                
                val response = apiService.syncNotes(syncRequest)
                if (response.isSuccessful) {
                    response.body()?.let { syncResponse ->
                        if (syncResponse.success) {
                            // Mark local notes as synced
                            unsyncedNotes.forEach { note ->
                                noteDao.markAsSynced(note.id)
                            }
                            
                            // Update local notes with server data
                            val serverNotes = syncResponse.notes.map { NoteMapper.dtoToDomain(it) }
                            noteDao.insertNotes(serverNotes.map { NoteMapper.domainToEntity(it) })
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Handle sync error - notes remain unsynced for retry
            throw e
        }
    }

    override suspend fun getUnsyncedNotes(): List<Note> {
        return noteDao.getUnsyncedNotes().map { NoteMapper.entityToDomain(it) }
    }
}