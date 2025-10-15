package com.example.notesapp.domain.usecase

import com.example.notesapp.domain.repository.NotesRepository
import javax.inject.Inject

class SyncNotesUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    suspend operator fun invoke() = repository.syncNotes()
}