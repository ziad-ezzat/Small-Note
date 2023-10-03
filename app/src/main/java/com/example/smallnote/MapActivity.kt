package com.example.smallnote

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Add a default marker (you can customize this based on your requirements)
        val defaultLocation = LatLng(0.0, 0.0)
        map.addMarker(MarkerOptions().position(defaultLocation).title("Default Location"))
        map.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation))

        // Set up a map click listener to capture the selected location
        map.setOnMapClickListener { latLng ->
            val resultIntent = Intent()
            resultIntent.putExtra("latitude", latLng.latitude)
            resultIntent.putExtra("longitude", latLng.longitude)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}