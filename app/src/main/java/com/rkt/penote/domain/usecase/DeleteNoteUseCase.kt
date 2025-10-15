package com.rkt.penote.domain.usecase

import com.rkt.penote.domain.model.Note
import com.rkt.penote.domain.repository.NotesRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(note: Note) = repository.deleteNote(note)
}