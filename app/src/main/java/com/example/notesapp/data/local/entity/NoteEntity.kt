package com.example.notesapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Date,
    val updatedAt: Date,
    val isSynced: Boolean = false
)