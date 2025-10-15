package com.rkt.penote.data.mapper

import com.rkt.penote.data.local.entity.NoteEntity
import com.rkt.penote.data.remote.dto.NoteDto
import com.rkt.penote.domain.model.Note
import java.text.SimpleDateFormat
import java.util.*

object NoteMapper {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    fun entityToDomain(entity: NoteEntity): Note {
        return Note(
            id = entity.id,
            title = entity.title,
            content = entity.content,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            isSynced = entity.isSynced
        )
    }

    fun domainToEntity(note: Note): NoteEntity {
        return NoteEntity(
            id = note.id,
            title = note.title,
            content = note.content,
            createdAt = note.createdAt,
            updatedAt = note.updatedAt,
            isSynced = note.isSynced
        )
    }

    fun dtoToDomain(dto: NoteDto): Note {
        return Note(
            id = dto.id,
            title = dto.title,
            content = dto.content,
            createdAt = dateFormat.parse(dto.createdAt) ?: Date(),
            updatedAt = dateFormat.parse(dto.updatedAt) ?: Date(),
            isSynced = true
        )
    }

    fun domainToDto(note: Note): NoteDto {
        return NoteDto(
            id = note.id,
            title = note.title,
            content = note.content,
            createdAt = dateFormat.format(note.createdAt),
            updatedAt = dateFormat.format(note.updatedAt)
        )
    }
}