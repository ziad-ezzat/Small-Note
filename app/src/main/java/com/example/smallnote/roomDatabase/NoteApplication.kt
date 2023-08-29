package com.example.smallnote.roomDatabase

import android.app.Application
import com.example.smallnote.roomDatabase.AppDatabase
import com.example.smallnote.roomDatabase.NoteRepo
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NoteApplication: Application() {

    private val database by lazy { AppDatabase.getInstance(this) }

    val repository by lazy { NoteRepo(database.noteDao()) }

}