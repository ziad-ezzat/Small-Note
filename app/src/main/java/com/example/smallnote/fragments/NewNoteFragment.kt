package com.example.smallnote.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.smallnote.R
import com.example.smallnote.roomDatabase.Note
import com.example.smallnote.roomDatabase.NoteApplication
import com.example.smallnote.roomDatabase.NoteViewModel
import com.example.smallnote.roomDatabase.NoteViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewNoteFragment : Fragment(R.layout.fragment_new_note) {

    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((requireActivity().application as NoteApplication).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        val buttonSave = view.findViewById<View>(R.id.button_save)
        val editWordView = view.findViewById<EditText>(R.id.edit_word)

        buttonSave.setOnClickListener {
            val newNote = editWordView.text.toString().trim()

            if (newNote.isEmpty()) {
                editWordView.error = "Please enter a note"
                return@setOnClickListener
            }

            noteViewModel.insert(Note(newNote))
            navController.navigateUp()
        }
    }
}