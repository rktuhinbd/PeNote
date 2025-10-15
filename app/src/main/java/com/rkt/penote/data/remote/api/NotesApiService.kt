package com.rkt.penote.data.remote.api

import com.rkt.penote.data.remote.dto.SyncRequestDto
import com.rkt.penote.data.remote.dto.SyncResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotesApiService {
    @GET("notes")
    suspend fun getAllNotes(): Response<List<com.rkt.penote.data.remote.dto.NoteDto>>

    @POST("notes/sync")
    suspend fun syncNotes(@Body request: SyncRequestDto): Response<SyncResponseDto>
}