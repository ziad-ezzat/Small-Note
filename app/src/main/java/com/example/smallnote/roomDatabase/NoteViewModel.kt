package com.example.smallnote.roomDatabase

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import kotlinx.coroutines.launch
import com.example.smallnote.workManager.*
import java.util.concurrent.TimeUnit

class NoteViewModel(private val appContext: Context, private val repo: NoteRepo): ViewModel() {

    val allNotes: LiveData<List<Note>> = repo.allNotes.asLiveData()

    fun insert(note: Note) = viewModelScope.launch {
        repo.insert(note)
    }

    public fun setupPeriodicWorkRequest() {
        val workRequest = PeriodicWorkRequest.Builder(
            PostNoteToFirebaseByRetrofitWorker::class.java, 2, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(appContext).enqueue(workRequest)
    }
}