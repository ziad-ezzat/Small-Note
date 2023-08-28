package com.example.smallnote.roomDatabase

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NoteViewModelFactory(private val repo: NoteRepo, private val appContext: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(appContext,repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}