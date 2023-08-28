package com.example.smallnote.workManager

import com.example.smallnote.roomDatabase.Note
import retrofit2.*
import retrofit2.http.Body
import retrofit2.http.POST

interface NoteApiService {
    @POST("notes.json")
    suspend fun postNote(@Body note: Note): Response<Note>
}