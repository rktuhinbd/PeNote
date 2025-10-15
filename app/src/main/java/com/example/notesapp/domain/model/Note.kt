package com.example.notesapp.domain.model

import java.util.Date

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Date,
    val updatedAt: Date,
    val isSynced: Boolean = false
)