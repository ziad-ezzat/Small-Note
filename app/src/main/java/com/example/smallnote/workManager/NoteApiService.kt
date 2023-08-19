package com.example.smallnote.workManager

import com.example.smallnote.roomDatabase.Note
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NoteApiService {
    @POST("notes.json")
    fun postNote(@Body note: Note): Call<Note>
}