package com.example.notesapp.data.remote.api

import com.example.notesapp.data.remote.dto.SyncRequestDto
import com.example.notesapp.data.remote.dto.SyncResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotesApiService {
    @GET("notes")
    suspend fun getAllNotes(): Response<List<com.example.notesapp.data.remote.dto.NoteDto>>

    @POST("notes/sync")
    suspend fun syncNotes(@Body request: SyncRequestDto): Response<SyncResponseDto>
}