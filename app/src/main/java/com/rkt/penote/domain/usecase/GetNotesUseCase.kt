package com.rkt.penote.domain.usecase

import com.rkt.penote.domain.model.Note
import com.rkt.penote.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    operator fun invoke(): Flow<List<Note>> = repository.getAllNotes()
}