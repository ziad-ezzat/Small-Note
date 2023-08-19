package com.example.smallnote.roomDatabase

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NoteViewModel(private val repo: NoteRepo): ViewModel() {

    val allNotes: LiveData<List<Note>> = repo.allNotes.asLiveData()

    fun insert(note: Note) = viewModelScope.launch {
        repo.insert(note)
    }
}