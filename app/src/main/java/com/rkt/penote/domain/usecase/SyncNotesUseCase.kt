package com.rkt.penote.domain.usecase

import com.rkt.penote.domain.repository.NotesRepository
import javax.inject.Inject

class SyncNotesUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    suspend operator fun invoke() = repository.syncNotes()
}