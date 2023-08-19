package com.example.smallnote.roomDatabase

import kotlinx.coroutines.flow.Flow

class NoteRepo(private val noteDao: NoteDao) {

    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }
}