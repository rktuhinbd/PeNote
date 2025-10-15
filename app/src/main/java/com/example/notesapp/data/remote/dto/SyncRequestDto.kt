package com.example.notesapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SyncRequestDto(
    @SerializedName("notes")
    val notes: List<NoteDto>
)

data class SyncResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("notes")
    val notes: List<NoteDto>
)