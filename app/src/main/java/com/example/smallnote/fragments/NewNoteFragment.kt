package com.example.smallnote.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.smallnote.MapActivity
import com.example.smallnote.R
import com.example.smallnote.roomDatabase.Note
import com.example.smallnote.roomDatabase.NoteApplication
import com.example.smallnote.roomDatabase.NoteViewModel
import com.example.smallnote.roomDatabase.NoteViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

class NewNoteFragment : Fragment(R.layout.fragment_new_note), OnMapReadyCallback {

    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((requireActivity().application as NoteApplication).repository)
    }

    private lateinit var mapView: MapView
    private lateinit var addMapButton: Button
    private lateinit var latitudeEditText: EditText
    private lateinit var longitudeEditText: EditText

    private var selectedLatitude: Double? = null
    private var selectedLongitude: Double? = null

    private val mapLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                val data = result.data
                if (data != null && data.hasExtra("latitude") && data.hasExtra("longitude")) {
                    selectedLatitude = data.getDoubleExtra("latitude", 0.0)
                    selectedLongitude = data.getDoubleExtra("longitude", 0.0)

                    updateLocationUI(selectedLatitude!!, selectedLongitude!!)
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        val buttonSave = view.findViewById<View>(R.id.button_save)
        addMapButton = view.findViewById(R.id.button_add_map)
        latitudeEditText = view.findViewById(R.id.latitudeEditText)
        longitudeEditText = view.findViewById(R.id.longitudeEditText)
        mapView = view.findViewById(R.id.mapView)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        buttonSave.setOnClickListener {
            saveNote()
            navController.navigateUp()
        }

        addMapButton.setOnClickListener {
            openMap()
        }
    }

    private fun openMap() {
        val mapIntent = Intent(requireContext(), MapActivity::class.java)
        mapLauncher.launch(mapIntent)
    }

    private fun updateLocationUI(latitude: Double, longitude: Double) {
        latitudeEditText.setText(latitude.toString())
        longitudeEditText.setText(longitude.toString())
    }

    private fun saveNote() {
        val editWordView = view?.findViewById<EditText>(R.id.edit_word)
        val newNote = editWordView?.text.toString().trim()

        if (newNote.isNotEmpty()) {
            noteViewModel.insert(Note(newNote, selectedLatitude, selectedLongitude))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setOnMapClickListener { latLng ->
            selectedLatitude = latLng.latitude
            selectedLongitude = latLng.longitude

            updateLocationUI(selectedLatitude!!, selectedLongitude!!)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}