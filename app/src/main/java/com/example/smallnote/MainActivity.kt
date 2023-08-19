package com.example.smallnote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.smallnote.roomDatabase.Note
import com.example.smallnote.roomDatabase.NoteApplication
import com.example.smallnote.roomDatabase.NoteViewModel
import com.example.smallnote.roomDatabase.NoteViewModelFactory
import com.example.smallnote.workManager.PostNoteToFirebaseByRetrofitWorker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val newWordActivityRequestCode = 1
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NoteApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NoteListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewNoteActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

        noteViewModel.allNotes.observe(this) { notes ->
            notes.let { adapter.submitList(it) }
        }

        val workRequest = PeriodicWorkRequest.Builder(
            PostNoteToFirebaseByRetrofitWorker::class.java, 2, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newWordActivityRequestCode && resultCode == RESULT_OK) {
            intentData?.getStringExtra("com.example.android.wordlist.REPLY")?.let { reply ->
                val note = Note(reply)
                noteViewModel.insert(note)
            }
        }
        else
        {
            Toast.makeText(
                applicationContext,
                "Note not saved because it is empty",
                Toast.LENGTH_LONG).show()
        }
    }
}