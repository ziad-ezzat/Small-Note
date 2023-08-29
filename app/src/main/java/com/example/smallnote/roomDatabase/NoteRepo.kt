package com.example.smallnote.roomDatabase

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepo @Inject constructor(private val noteDao: NoteDao) {

    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }
}