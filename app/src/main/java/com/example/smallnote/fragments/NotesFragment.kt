package com.example.smallnote.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.smallnote.NoteListAdapter
import com.example.smallnote.R
import com.example.smallnote.roomDatabase.NoteApplication
import com.example.smallnote.roomDatabase.NoteViewModel
import com.example.smallnote.roomDatabase.NoteViewModelFactory
import com.example.smallnote.workManager.PostNoteToFirebaseByRetrofitWorker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_notes) {

    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((requireActivity().application as NoteApplication).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        setupFabButton(view)
        noteViewModel.setupPeriodicWorkRequest()
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NoteListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        noteViewModel.allNotes.observe(viewLifecycleOwner, Observer { notes ->
            adapter.submitList(notes)
        })
    }

    private fun setupFabButton(view: View) {
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_notesFragment_to_newNoteFragment)
        }
    }
}